import os, re, sys

d = os.path.join(os.environ.get("HARNESS_SCRATCH", ""), "lucide")
out = sys.argv[1]

# Public Kotlin symbol (Ic-prefix) -> lucide svg filename
names = {
 "Home":"house","Compass":"compass","Layers":"layers",
 "Cart":"shopping-cart","Bag":"shopping-bag","Search":"search",
 "Sliders":"sliders-horizontal","Bell":"bell",
 "ChevronDown":"chevron-down","ChevronRight":"chevron-right","ChevronLeft":"chevron-left",
 "X":"x","User":"user","Settings":"settings","MapPin":"map-pin","Heart":"heart",
 "Box":"box","Truck":"truck","BadgePercent":"badge-percent","CreditCard":"credit-card",
 "Logout":"log-out","ShieldCheck":"shield-check","Star":"star","Plus":"plus","Minus":"minus",
 "Check":"check","Copy":"copy","ArrowRight":"arrow-right","Clock":"clock","Package":"package",
 "Info":"info","Phone":"phone","Lock":"lock","Smartphone":"smartphone","Calendar":"calendar",
 "Flame":"flame","Sparkles":"sparkles","Zap":"zap","Tag":"tag","Percent":"percent",
 "List":"list","Menu":"menu","Grid":"layout-grid","RotateCcw":"rotate-ccw","Wallet":"wallet",
 "Utensils":"utensils","Basket":"shopping-basket","Store":"store","BadgeCheck":"badge-check",
 "Receipt":"receipt","PackageCheck":"package-check","Timer":"timer","Car":"car","Gift":"gift",
 "Message":"message-circle","Thumb":"thumbs-up","Trash":"trash","Scale":"scale",
 "CheckCircle":"circle-check-big","CircleX":"circle-x","CirclePercent":"circle-percent",
 "LifeBuoy":"life-buoy","Headphones":"headphones",
 # category/product glyphs
 "Soda":"cup-soda","Apple":"apple","Cookie":"cookie","Milk":"milk","Carrot":"carrot",
 "Droplets":"droplets","Pencil":"pencil","Cpu":"cpu","Sprout":"sprout","Leaf":"leaf",
 "Coffee":"coffee","Wheat":"wheat","QrCode":"qr-code","Rupee":"indian-rupee",
 "Banknote":"banknote","Inbox":"inbox","KeyRound":"key-round","Weight":"weight","Map":"map",
 "Bean":"bean","Cake":"cake","Candy":"candy","Cherry":"cherry","Citrus":"citrus",
 "Wine":"wine","Egg":"egg","Soup":"soup","GlassWater":"glass-water",
 "CookingPot":"cooking-pot","Shell":"shell","SprayCan":"spray-can","Syringe":"syringe",
 "Flashlight":"flashlight","Banana":"banana","Croissant":"croissant","Ruler":"ruler",
 "ChefHat":"chef-hat","Drumstick":"drumstick","Beef":"beef","Donut":"donut",
 "Nut":"nut","Salad":"salad","IceCreamCone":"ice-cream-cone",
}

def fnum(s, default=0.0):
    if s is None: return default
    s = s.strip()
    if not s: return default
    try: return float(s)
    except: return default

def attrs(raw):
    return dict(re.findall(r'([a-zA-Z-]+)\s*=\s*"([^"]*)"', raw))

def svg_paths(f):
    raw = open(os.path.join(d, f + ".svg")).read().replace("\n", " ")
    out = []
    for m in re.finditer(r'<(path|line|rect|polyline|polygon|circle)\b([^>]*?)/?>', raw):
        tag, a = m.group(1), attrs(m.group(2))
        if tag == "path":
            pd = a.get("d", "").strip()
            if pd: out.append(pd)
        elif tag == "line":
            out.append("M %g %g L %g %g" % (fnum(a.get("x1")), fnum(a.get("y1")), fnum(a.get("x2")), fnum(a.get("y2"))))
        elif tag == "rect":
            x=fnum(a.get("x")); y=fnum(a.get("y")); w=fnum(a.get("width")); h=fnum(a.get("height"))
            rx=fnum(a.get("rx")); ry=fnum(a.get("ry"), rx)
            if rx==0 or w==0 or h==0:
                out.append("M %g %g L %g %g L %g %g L %g %g Z" % (x,y,x+w,y,x+w,y+h,x,y+h))
            else:
                out.append("M %g %g L %g %g Q %g %g %g %g L %g %g Q %g %g %g %g L %g %g Q %g %g %g %g L %g %g Q %g %g %g %g Z" % (
                    x+rx,y, x+w-rx,y, x+w,y, x+w,y+ry, x+w,y+h-ry, x+w,y+h, x+w-rx,y+h, x+rx,y+h,
                    x,y+h, x,y+h-ry, x,y+ry, x,y, x+rx,y))
        elif tag in ("polyline","polygon"):
            pts = [fnum(t) for t in a.get("points","").split()]
            c = [(pts[i],pts[i+1]) for i in range(0, len(pts)-1, 2)]
            if c:
                s = "M %g %g " % c[0] + " ".join("L %g %g" % p for p in c[1:])
                if tag == "polygon": s += " Z"
                out.append(s)
        elif tag == "circle":
            cx=fnum(a.get("cx")); cy=fnum(a.get("cy")); r=fnum(a.get("r"))
            if r>0:
                out.append("M %g %g a %g %g 0 1 0 %g 0 a %g %g 0 1 0 %g 0" % (cx-r,cy,r,r,2*r,r,r,-2*r))
    return out

def kstr(s): return s.replace("\\","\\\\").replace('"','\\"')

L = []
L.append("package canteenconnect.app.core.icons")
L.append("")
L.append("import androidx.compose.foundation.Canvas")
L.append("import androidx.compose.foundation.layout.size")
L.append("import androidx.compose.runtime.Composable")
L.append("import androidx.compose.runtime.remember")
L.append("import androidx.compose.ui.Modifier")
L.append("import androidx.compose.ui.graphics.Color")
L.append("import androidx.compose.ui.graphics.Paint")
L.append("import androidx.compose.ui.graphics.PaintingStyle")
L.append("import androidx.compose.ui.graphics.Path")
L.append("import androidx.compose.ui.graphics.StrokeCap")
L.append("import androidx.compose.ui.graphics.StrokeJoin")
L.append("import androidx.compose.ui.graphics.drawscope.drawIntoCanvas")
L.append("import androidx.compose.ui.graphics.vector.PathParser")
L.append("import androidx.compose.ui.unit.Dp")
L.append("import androidx.compose.ui.unit.dp")
L.append("")
L.append("// Opshar icon set: Lucide-derived outline icons drawn as stroke paths.")
L.append("// 24-unit grid, rounded caps/joins, one consistent style across the app.")
L.append("")
L.append("internal data class IconData(val name: String, val pathData: List<String>)")
L.append("")
L.append("@Composable")
L.append("internal fun opsharIcon(")
L.append("    data: IconData,")
L.append("    modifier: Modifier = Modifier,")
L.append("    tint: Color = Color(0xFF24203A),")
L.append("    iconSize: Dp = 24.dp,")
L.append("    strokeWidth: Float = 2f,")
L.append(") {")
L.append("    val paths = remember(data) {")
L.append("        data.pathData.map { d ->")
L.append("            Path().apply { PathParser().apply { parsePathString(d) }.toPath(this) }")
L.append("        }")
L.append("    }")
L.append("    Canvas(modifier = Modifier.size(iconSize).then(modifier)) {")
L.append("        val s = (size.width / 24f).coerceAtLeast(0f)")
L.append("        drawIntoCanvas { canvas ->")
L.append("            canvas.save()")
L.append("            canvas.scale(s, s)")
L.append("            val paint = Paint().apply {")
L.append("                style = PaintingStyle.Stroke")
L.append("                color = tint")
L.append("                this.strokeWidth = strokeWidth")
L.append("                strokeCap = StrokeCap.Round")
L.append("                strokeJoin = StrokeJoin.Round")
L.append("                isAntiAlias = true")
L.append("            }")
L.append("            paths.forEach { canvas.drawPath(it, paint) }")
L.append("            canvas.restore()")
L.append("        }")
L.append("    }")
L.append("}")
L.append("")

for base, f in names.items():
    if not os.path.exists(os.path.join(d, f + ".svg")):
        print("MISSING", base, f); continue
    paths = svg_paths(f)
    if not paths: paths = ["M 6 6 L 18 18"]
    literal = ", ".join('"%s"' % kstr(p) for p in paths)
    L.append("internal val Ic%s_DATA = IconData(\"%s\", listOf(%s))" % (base, base, literal))
L.append("")

for base in names:
    if not os.path.exists(os.path.join(d, names[base] + ".svg")): continue
    L.append("@Composable")
    L.append("fun Ic%s(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =" % base)
    L.append("    opsharIcon(Ic%s_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)" % base)
    L.append("")

open(out, "w").write("\n".join(L) + "\n")
print("WROTE", out, os.path.getsize(out), "bytes,", len(names), "icon composables")
