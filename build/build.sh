#!/system/bin/sh
# Canteen Mitra — on-device APK build (Gradle-free pipeline).
# aapt2(res+libs) -> javac(app R) + generated lib R classes -> kotlinc x2 -> d8 -> apksigner
set -eu
W=/storage/emulated/0/Android/data/com.androidharness.app/files/workspace/ccapp
P=/data/user/0/com.androidharness.app/files/linux
S=/data/user/0/com.androidharness.app/files/.harness-scratch
SRC="$W/app/src/main"
OUT="$W/build"
DEPS="$W/build/deps"
JVM="$P/lib/jvm/java-21-openjdk/bin"
export PATH="$JVM:$PATH"
export ANDROID_JAR="$P/home/android-sdk/platforms/android-35/android.jar"
KEYSTORE="$P/home/.android/debug.keystore"

# ---- 0) flatten dependency classes + library resources (once) ----
LIBRZ="$OUT/libres"
if [ ! -f "$OUT/flats/classes/.done" ]; then
  rm -rf "$OUT/flats" "$OUT/jars"
  mkdir -p "$OUT/flats/classes" "$OUT/jars"
  cd "$DEPS"
  for a in compose-*.aar lifecycle-*.aar androidx-*.aar; do
    [ -f "$a" ] || continue
    tmp=$S/aar_x; rm -rf "$tmp"; mkdir -p "$tmp"
    unzip -o -q "$a" -d "$tmp" 2>/dev/null || true
    for j in $(find "$tmp" -name '*.jar' -not -path '*META-INF*'); do
      unzip -o -q "$j" -d "$OUT/flats/classes" 2>/dev/null || true
    done
    rm -rf "$tmp"
  done
  # runtime jars: keep whole in build/jars (extracted into the tree at dex time)
  for j in kotlin-stdlib.jar kotlin-reflect.jar jb-annotations.jar coroutines-core-jvm.jar \
           coroutines-android.jar annotation.jar lifecycle-common.jar arch-core-common.jar collection.jar \
           atomicfu.jar; do
    [ -f "$j" ] && cp -f "$j" "$OUT/jars/"
  done
  # plain runtime resource+class jars extracted directly
  for j in androidx-concurrent-futures.jar guava-listenablefuture.jar; do
    [ -f "$j" ] && unzip -o -q "$j" -d "$OUT/flats/classes" 2>/dev/null || true
  done
  ls "$OUT/jars/kotlin-stdlib.jar" >/dev/null || { echo "FATAL: jars not populated"; exit 1; }
  echo done > "$OUT/flats/classes/.done"
fi

# library resources -> compiled zips (cached)
if [ ! -f "$LIBRZ/.done" ]; then
  rm -rf "$LIBRZ"; mkdir -p "$LIBRZ"
  for a in "$DEPS"/*.aar; do
    b=$(basename "$a")
    tmp=$S/aar_r; rm -rf "$tmp"; mkdir -p "$tmp"
    unzip -o -q "$a" -d "$tmp" 2>/dev/null || true
    if [ -d "$tmp/res" ]; then
      aapt2 compile --dir "$tmp/res" -o "$LIBRZ/$b.zip" 2>/dev/null || true
    fi
    rm -rf "$tmp"
  done
  echo done > "$LIBRZ/.done"
fi

rm -rf "$OUT/gen" "$OUT/libRgen" "$OUT/libR" "$OUT/classes1" "$OUT/classes2" "$OUT/dex" "$OUT/app.jar" "$OUT/deps.jar"
mkdir -p "$OUT/dex"

# ---- 1+2) resources + link (with library res merged, emit ids for R generation) ----
aapt2 compile --dir "$SRC/res" -o "$OUT/res.zip"
LIBZIPS=""
for z in "$LIBRZ"/*.zip; do [ -f "$z" ] && LIBZIPS="$LIBZIPS $z"; done
# shellcheck disable=SC2086
aapt2 link -I "$ANDROID_JAR" --manifest "$SRC/AndroidManifest.xml" \
  --min-sdk-version 26 --target-sdk-version 35 \
  --version-code 1 --version-name 1.0.0 \
  --emit-ids "$OUT/ids.txt" -o "$OUT/base.apk" "$OUT/res.zip" $LIBZIPS

# ---- 3) app R.java ----
# Generate the app-level R class (canteenconnect.app.R) from ids.txt so Kotlin
# can reference R.drawable / R.string resource ids. gen_r.py handles libraries;
# this covers the app package itself.
python3 "$W/build/gen_appr.py" "$OUT"
# R.java has no android/androidx imports -> plain javac (a -bootclasspath
# android.jar invocation fails on modern JDKs and was silently dropped before).
javac -d "$OUT/classes1" $(find "$OUT/gen" -name 'R.java' 2>/dev/null) || echo "[R] app R javac FAILED"

# ---- 3b) library R classes (AGP non-transitive R) ----
cd "$OUT"
for a in "$DEPS"/*.aar; do
  b=$(basename "$a" .aar)
  pkg=$(unzip -p "$a" AndroidManifest.xml 2>/dev/null | grep -oE 'package="[^"]+"' | head -1 | sed 's/package="//; s/"//')
  rtxt=$(unzip -p "$a" R.txt 2>/dev/null)
  [ -n "$pkg" ] && [ -n "$rtxt" ] && { echo "$pkg" > "$OUT/pkg_$b.txt"; printf '%s' "$rtxt" > "$OUT/rtxt_$b.txt"; }
done
rm -rf "$OUT/libRgen"
python3 "$W/build/gen_r.py" "$OUT"
if [ -d "$OUT/libRgen" ]; then
  # generated lib R classes are dependency-free (no android/androidx imports) -> plain javac
  # (-bootclasspath android.jar breaks on JDK21 default target: "not allowed with target 21")
  javac -source 8 -target 8 -d "$OUT/libR" $(find "$OUT/libRgen" -name 'R.java') 2>/dev/null
  echo "[R] libR classes: $(find $OUT/libR -name '*.class' 2>/dev/null | wc -l)"
fi

# ---- 4) kotlin, pass 1 ----
KCP="$DEPS/compose-compiler-plugin.jar"
CP="$ANDROID_JAR"
for j in "$OUT/jars"/*.jar; do [ -f "$j" ] && CP="$CP:$j"; done
CP="$CP:$OUT/flats/classes:$OUT/classes1"
KDIR="$S/kotlin-compiler-2.3.0"
KCPATH="$KDIR:$OUT/jars/kotlin-stdlib.jar:$OUT/jars/kotlin-reflect.jar:$OUT/jars/coroutines-core-jvm.jar:$OUT/jars/jb-annotations.jar"
echo "[kotlinc] pass 1 (core/data/domain)"
java -Xmx1500m -cp "$KCPATH" org.jetbrains.kotlin.cli.jvm.K2JVMCompiler \
  -classpath "$CP" -Xplugin="$KCP" \
  -jvm-target 17 -module-name canteenconnect -no-stdlib -nowarn \
  -d "$OUT/classes1" $(find "$SRC/java" -name '*.kt' -not -path '*features*' -not -name 'MainActivity.kt') || exit 1

# ---- 5) kotlin, pass 2 ----
echo "[kotlinc] pass 2 (features)"
java -Xmx1500m -cp "$KCPATH" org.jetbrains.kotlin.cli.jvm.K2JVMCompiler \
  -classpath "$CP:$OUT/classes1" -Xplugin="$KCP" \
  -jvm-target 17 -module-name canteenconnect -no-stdlib -nowarn \
  -d "$OUT/classes2" $(find "$SRC/java" -name '*.kt' -path '*features*') "$SRC/java/canteenconnect/app/MainActivity.kt" || exit 1

# ---- 6) dex ----
(cd "$OUT" && jar cfM app.jar -C classes1 . && jar ufM app.jar -C classes2 . && [ -d libR ] && jar ufM app.jar -C libR .)
cd "$OUT/flats/classes"
find . -name '*.dex' -delete
for j in "$OUT/jars"/*.jar; do
  b="$(basename "$j")"
  case "$b" in
    kotlin-reflect*|jb-annotations*) : ;;
    *) unzip -o -q "$j" -d . 2>/dev/null || true ;;
  esac
done
find . -maxdepth 1 -name '*.jar' -delete
jar cfM "$OUT/deps.jar" .
echo "[d8] deps.jar: $(du -h $OUT/deps.jar | cut -f1)"
[ -d "$OUT/flats/classes/kotlin" ] || { echo "FATAL: kotlin-stdlib missing"; exit 1; }
[ -f "$OUT/flats/classes/androidx/lifecycle/LifecycleOwner.class" ] || { echo "FATAL: androidx.lifecycle.LifecycleOwner missing from class tree (lifecycle-common is KMP-only?)"; exit 1; }
[ -f "$OUT/flats/classes/androidx/core/view/ViewKt.class" ] || { echo "FATAL: androidx.core.view.ViewKt missing (stage deps/androidx-core-ktx.aar?)"; exit 1; }
[ -f "$OUT/jars/atomicfu.jar" ] || { echo "FATAL: atomicfu.jar not staged (coroutines runtime dep)"; exit 1; }
echo "[d8] dexing"
java -Xmx3000m -cp "$P/share/java/d8.jar" com.android.tools.r8.D8 \
  --min-api 26 --lib "$ANDROID_JAR" --output "$OUT/dex" "$OUT/app.jar" "$OUT/deps.jar"

# ---- 7) assemble ----
cp "$OUT/base.apk" "$OUT/unsigned.apk"
for dex in "$OUT/dex"/classes*.dex; do
  jar --update --file "$OUT/unsigned.apk" -C "$OUT/dex" "$(basename "$dex")"
done
if unzip -l "$OUT/unsigned.apk" 2>/dev/null | grep -q '\.so'; then
  aapt2 zipalign -p -f -o "$OUT/aligned.apk" "$OUT/unsigned.apk" 2>/dev/null && mv "$OUT/aligned.apk" "$OUT/unsigned.apk"
fi

# ---- 8) sign ----
[ -f "$KEYSTORE" ] || { mkdir -p "$(dirname "$KEYSTORE")"
  keytool -genkeypair -keystore "$KEYSTORE" -storepass android -keypass android \
    -alias androiddebugkey -keyalg RSA -keysize 2048 -validity 10000 \
    -dname "CN=Android Debug,O=Android,C=US" 2>/dev/null || true; }
export KS_PASS=android
apksigner sign --ks "$KEYSTORE" --ks-key-alias androiddebugkey \
  --ks-pass env:KS_PASS --key-pass env:KS_PASS \
  --out "$W/build/app-debug.apk" "$OUT/unsigned.apk"
apksigner verify "$W/build/app-debug.apk" && echo "APK OK: $W/build/app-debug.apk ($(wc -c < $W/build/app-debug.apk) bytes)"
# convenience copy at repo root (keeps install docs accurate; previously went stale)
cp -f "$W/build/app-debug.apk" "$W/app-debug.apk"
