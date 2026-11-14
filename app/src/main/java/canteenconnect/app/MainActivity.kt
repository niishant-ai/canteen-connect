package canteenconnect.app

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.ComposeView
import canteenconnect.app.core.AppStore
import canteenconnect.app.core.components.FloatingBottomNav
import canteenconnect.app.core.icons.IcBag
import canteenconnect.app.core.navigation.Nav
import canteenconnect.app.core.theme.Brand
import canteenconnect.app.core.theme.O
import canteenconnect.app.core.theme.OpsharTheme
import canteenconnect.app.features.canteens.CanteenDetailScreen
import canteenconnect.app.features.cart.CartScreen
import canteenconnect.app.features.categories.CategoryScreen
import canteenconnect.app.features.checkout.CheckoutScreen
import canteenconnect.app.features.explore.ExploreScreen
import canteenconnect.app.features.home.HomeScreen
import canteenconnect.app.features.location.LocationScreen
import canteenconnect.app.features.membership.MembershipScreen
import canteenconnect.app.features.notifications.NotificationsScreen
import canteenconnect.app.features.offers.OffersScreen
import canteenconnect.app.features.onboarding.OnboardingScreen
import canteenconnect.app.features.orders.OrderDetailScreen
import canteenconnect.app.features.orders.OrderSuccessScreen
import canteenconnect.app.features.orders.OrdersScreen
import canteenconnect.app.features.products.ProductDetailScreen
import canteenconnect.app.features.profile.AboutScreen
import canteenconnect.app.features.profile.HelpScreen
import canteenconnect.app.features.profile.PrivacyScreen
import canteenconnect.app.features.profile.ProfileScreen
import canteenconnect.app.features.profile.SettingsScreen
import canteenconnect.app.features.profile.TermsScreen
import canteenconnect.app.features.search.SearchScreen

class MainActivity : ComponentActivity() {
    // ComponentActivity (androidx.activity) is both a LifecycleOwner and a
    // ViewModelStoreOwner, and its setContentView(View) auto-installs the
    // ViewTree owners (ViewTreeLifecycleOwner / ViewTreeViewModelStoreOwner) on the
    // content view. Compose's lifecycle-aware WindowRecomposer requires those to be
    // reachable on the ComposeView's view tree, so deriving from ComponentActivity
    // (instead of a plain android.app.Activity) is what lets Compose launch. There is
    // nothing else to wire by hand here.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        AppStore.init(applicationContext)
        val view = ComposeView(this)
        view.setContent {
            // AURA is a light-first design system: always run the light theme so
            // warm sand surfaces, sage accents and light header bands render even
            // when the device is in system dark mode (the dark set is unfinished).
            OpsharTheme(dark = false) {
                OpsharAppRoot()
            }
        }
        // triggers ComponentActivity.initializeViewTreeOwners() on `view`
        setContentView(view)
        AppStore.attachView(view)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!NavBridge.tryBack()) super.onBackPressed()
    }
}

/** Bridges system back into the Compose-managed stack. */
object NavBridge {
    @Volatile
    var handler: (() -> Boolean)? = null
    @Synchronized
    fun tryBack(): Boolean = handler?.invoke() ?: false
}

@Composable
fun OpsharAppRoot() {
    val s = AppStore.state()

    var stack by remember {
        mutableStateOf(
            when {
                !s.onboardingDone -> listOf(Nav.ONBOARDING)
                !s.entered -> listOf(Nav.MEMBERSHIP)
                !s.locationSet -> listOf(Nav.LOCATION)
                else -> listOf(Nav.HOME)
            }
        )
    }

    // keep the stack consistent when the store catches up (e.g. user entered earlier this session)
    LaunchedEffect(s.onboardingDone, s.entered, s.locationSet) {
        if (stack.firstOrNull() == Nav.ONBOARDING && s.onboardingDone) {
            stack = when {
                !s.entered -> listOf(Nav.MEMBERSHIP)
                !s.locationSet -> listOf(Nav.LOCATION)
                else -> listOf(Nav.HOME)
            }
        }
        // after logout, return to the sign-in gate
        if (s.orders.isEmpty() && !s.entered && stack.firstOrNull() != Nav.MEMBERSHIP
            && !s.onboardingDone == false && s.onboardingDone) {
            // no-op guard; logout() resets entered so next launch lands on membership
        }
    }

    // register system back handler
    LaunchedEffect(Unit) {
        NavBridge.handler = {
            if (stack.size > 1) { stack = stack.dropLast(1); true }
            else if (stack.firstOrNull() !in Nav.bottomTabs.map { it.route }) { stack = listOf(Nav.HOME); true }
            else false
        }
    }

    // short branded splash on first compose
    var showSplash by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if (!AppStore.stateSnapshot.settingsReducedMotion) kotlinx.coroutines.delay(950)
        showSplash = false
    }
    if (showSplash) {
        SplashScreen()
        return
    }

    val current = stack.last()
    val onBack: () -> Unit = { if (stack.size > 1) stack = stack.dropLast(1) }
    val onNav: (String) -> Unit = { stack = stack + it }

    // Large-screen guard: on tablets the whole screen column is capped and
    // centred (see AnimatedContent below); the root paint keeps the margins
    // on-brand instead of showing the window background.
    Box(Modifier.fillMaxSize().background(O.Bg)) {
        // Large-screen guard: on wide displays each screen is capped at 720dp
        // and centred; the outer Box paints the full-bleed brand background so
        // the side margins stay on-brand. No-op on phones (< 720dp).
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = current,
                label = "screen",
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 720.dp)
            ) { route ->
                Screen(route = route, onNav = onNav, onBack = onBack)
            }
        }

        if (Nav.bottomTabs.any { it.route == current }) {
            FloatingBottomNav(
                current = current,
                onSelect = { tab -> if (tab != current) stack = listOf(tab) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    // Stay aligned with the capped content column on wide
                    // displays instead of spanning the full screen.
                    .widthIn(max = 720.dp)
                    // Edge-to-edge (targetSdk 35): keep the floating nav above the
                    // system navigation bar / gesture area, not behind it.
                    .navigationBarsPadding()
                    .padding(bottom = 12.dp)
            )
        }
    }
}

@Composable
private fun Screen(route: String, onNav: (String) -> Unit, onBack: () -> Unit) {
    when (route) {
        Nav.ONBOARDING -> OnboardingScreen { onNav(Nav.MEMBERSHIP) }
        Nav.MEMBERSHIP -> MembershipScreen { onNav(Nav.LOCATION) }
        Nav.LOCATION -> LocationScreen { onNav(Nav.HOME) }
        Nav.HOME -> HomeScreen(onNav)
        Nav.EXPLORE -> ExploreScreen(onNav)
        Nav.ORDERS -> OrdersScreen(onNav)
        Nav.CART -> CartScreen(onNav, onBack)
        Nav.PROFILE -> ProfileScreen(onNav)
        Nav.OFFERS -> OffersScreen(onNav, onBack)
        Nav.Search -> SearchScreen(onNav, onBack)
        Nav.Checkout -> CheckoutScreen(onNav, onBack)
        Nav.Notifications -> NotificationsScreen(onBack)
        Nav.Settings -> SettingsScreen(onNav, onBack)
        Nav.Help -> HelpScreen(onNav, onBack)
        Nav.About -> AboutScreen(onNav, onBack)
        Nav.Privacy -> PrivacyScreen(onBack)
        Nav.Terms -> TermsScreen(onBack)
        else -> {
            val parts = route.split("/")
            when (parts.firstOrNull()) {
                "canteen" -> CanteenDetailScreen(parts.getOrElse(1) { "" }, onNav, onBack)
                "category" -> CategoryScreen(parts.getOrElse(1) { "grocery" }, onNav, onBack)
                "productdetail" -> ProductDetailScreen(parts.getOrElse(1) { "" }, onNav, onBack)
                "ordersuccess" -> OrderSuccessScreen(parts.getOrElse(1) { "" }, onNav)
                "order" -> OrderDetailScreen(parts.getOrElse(1) { "" }, onNav, onBack)
                else -> HomeScreen(onNav)
            }
        }
    }
}

/** Branded Opshar splash: bag mark on a blue gradient tile + wordmark. */
@Composable
private fun SplashScreen() {
    val a = remember { androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(Unit) { a.animateTo(1f, tween(700)) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(O.Bg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(a.value.coerceAtLeast(0.001f)).alpha(a.value)
        ) {
            Box(
                modifier = Modifier
                    .size(104.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(listOf(O.Blue, O.Charcoal)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                IcBag(size = 50.dp, tint = Color.White, strokeWidth = 1.7f)
            }
            Spacer(Modifier.height(16.dp))
            Text(Brand.NAME, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold,
                letterSpacing = 6.sp, color = O.Ink)
            Text(Brand.TAGLINE, fontSize = 13.sp, color = O.Slate)
        }
    }
}
