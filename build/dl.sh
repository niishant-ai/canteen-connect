#!/system/bin/sh
G=https://dl.google.com/android/maven2
M=https://repo1.maven.org/maven2
D=/storage/emulated/0/Android/data/com.androidharness.app/files/workspace/ccapp/build/deps
S=/data/user/0/com.androidharness.app/files/.harness-scratch
mkdir -p $D
dl() { [ -f "$D/$1" ] || curl -sSfL -o "$D/$1" "$2" || echo "FAIL $1"; }
C=$G/androidx/compose
for a in ui ui-text ui-graphics ui-unit ui-geometry ui-layout ui-util; do dl "compose-$a.aar" $C/$a/$a/1.8.0/$a-1.8.0.aar; done
for a in runtime runtime-saveable; do dl "compose-$a.aar" $C/runtime/$a/1.8.0/$a-1.8.0.aar; done
for a in foundation foundation-layout animation animation-core; do dl "compose-$a.aar" $C/foundation/$a/1.8.0/$a-1.8.0.aar; done
dl compose-ripple.aar $C/material/material-ripple/1.8.0/material-ripple-1.8.0.aar
dl compose-material3.aar $C/material3/material3/1.3.0/material3-1.3.0.aar
dl compose-icons-core.aar $C/material/material-icons-core/1.7.5/material-icons-core-1.7.5.aar
dl compose-icons-extended.aar $C/material/material-icons-extended/1.7.5/material-icons-extended-1.7.5.aar
# lifecycle pinned at 2.8.7 — the baseline Compose 1.8.0's POM targets.
# CRITICAL: from 2.8.0 on, dl.google.com's KMP *root* artifacts (lifecycle-runtime,
# lifecycle-viewmodel, ...) are METADATA-ONLY AARs (~300 B, no classes.jar), so the
# real Android classes live in the '-android' variant artifacts and the common base
# classes in 'lifecycle-common-jvm'. Getting the KMP roots wrong is exactly what
# caused both launch crashes: the 2.9.x KMP roots drop androidx.lifecycle.
# LifecycleOwner from the dex (ClassNotFoundException), and the plain-JVM 2.7.0
# fallback drops lifecycle.compose.LocalLifecycleOwnerKt that Compose 1.8.0's
# AndroidComposeView requires at runtime. The '-android' variants below all carry
# real classes (verified: -android metadata http=200, classes.jar present).
# livedata-core has no -android variant at 2.8.7, so use its (real) root AAR.
for a in runtime viewmodel viewmodel-compose runtime-compose; do dl "lifecycle-$a.aar" $G/androidx/lifecycle/lifecycle-${a}-android/2.8.7/lifecycle-${a}-android-2.8.7.aar; done
dl lifecycle-livedata-core.aar $G/androidx/lifecycle/lifecycle-livedata-core/2.8.7/lifecycle-livedata-core-2.8.7.aar
dl lifecycle-common.jar $G/androidx/lifecycle/lifecycle-common-jvm/2.8.7/lifecycle-common-jvm-2.8.7.jar
# core-ktx carries the KTX extension facade (androidx.core.view.ViewKt with
# getAncestors/getAllViews) that customview-poolingcontainer 1.1.0 calls on view
# detach. The plain core.aar does NOT ship these KTX facade classes, so omitting
# core-ktx crashes at activity-destroy: NoClassDefFoundError androidx/core/view/ViewKt.
# Named 'androidx-core-ktx.aar' so build.sh's 'androidx-*.aar' glob stages its classes.
dl androidx-core-ktx.aar $G/androidx/core/core-ktx/1.13.1/core-ktx-1.13.1.aar
dl arch-core-common.jar $G/androidx/arch/core/core-common/2.2.0/core-common-2.2.0.jar
dl collection.aar $G/androidx/collection/collection/1.4.4/collection-1.4.4.aar
dl annotation.jar $G/androidx/annotation/annotation/1.9.1/annotation-1.9.1.jar
dl concurrent-futures.aar $G/androidx/concurrent/concurrent-futures/1.1.0/concurrent-futures-1.1.0.aar
dl coroutines-core.aar $M/org/jetbrains/kotlinx/kotlinx-coroutines-core/1.10.1/kotlinx-coroutines-core-1.10.1.aar
dl coroutines-android.aar $M/org/jetbrains/kotlinx/kotlinx-coroutines-android/1.10.1/kotlinx-coroutines-android-1.10.1.aar
# coroutines-core-jvm .class files soft-reference kotlinx/atomicfu (AtomicBoolean/
# AtomicInt/AtomicLong/AtomicRef/AtomicArray) — D8 needs these at runtime. Correct
# coordinate is org.jetbrains.kotlinx:atomicfu-jvm (artifactId 'atomicfu-jvm', NOT
# 'kotlinx-atomicfu-jvm', which is a 404).
dl atomicfu.jar $M/org/jetbrains/kotlinx/atomicfu-jvm/0.26.1/atomicfu-jvm-0.26.1.jar
[ -f $S/kotlin-compiler-2.3.0 ] || curl -sSfL -o $S/kotlinc.zip $M/org/jetbrains/kotlin/kotlin-compiler/2.3.0/kotlin-compiler-2.3.0.jar || echo "FAIL kotlinc-jar"
if [ -d $S/kotlin-compiler-2.3.0 ]; then echo kotlinc ready; else unzip -o -q $S/kotlinc.zip -d $S/kotlin-compiler-2.3.0 && echo kotlinc unpacked; fi
dl compose-compiler-plugin.jar $M/org/jetbrains/kotlin/kotlin-compose-compiler-plugin/2.3.0/kotlin-compose-compiler-plugin-2.3.0.jar
dl kotlin-stdlib.jar $M/org/jetbrains/kotlin/kotlin-stdlib/2.3.0/kotlin-stdlib-2.3.0.jar
ls $D | wc -l; ls $S/kotlin-compiler-2.3.0/bin 2>/dev/null | head -3
echo DONE_DL
