# Canteen Mitra — Build & Run

## What works on this device
The full Android Gradle toolchain cannot run here (Gradle's jextractor needs
`libstdc++.so.6`, which is unavailable in this environment — verified by direct
load test). The project is therefore built with a **manual pipeline** that produces a
real, installable, signed debug APK:

`aapt2 (resources) → javac (R.java) → kotlinc 2.3.0 + Compose compiler plugin
(app) → D8 (dex) → apksigner (sign/verify)`

All tools live in the on-device Linux toolchain:
- OpenJDK 21 (`$PREFIX/lib/jvm/java-21-openjdk/bin`)
- kotlinc 2.3.0 (`$HARNESS_SCRATCH/kotlin-compiler-2.3.0` — fat compiler jar +
  stdlib/reflect/coroutines/annotations on its classpath)
- Compose 1.7.6, material3 1.3.0, lifecycle 2.9/2.11, coroutines 1.10.1
  (fetched as AARs; classes extracted into `ccapp/build/flats/classes`)
- D8, aapt2, apksigner from the package repo; `android.jar` API 35

## Build
```sh
sh ccapp/build/build.sh
```
Outputs:
- `ccapp/build/app-debug.apk` — signed, verified
- convenience copy: `ccapp/app-debug.apk`

Re-runs are incremental only for downloads; compilation always recompiles.
First full run ≈ 3–4 min (2× kotlinc + D8).

## Build on a regular Linux machine
`build/build_linux.sh` runs the same pipeline with tool paths from the environment
(no Gradle). Tools used for the current APK: aapt2 8.7.3-12006047 (linux),
D8 from r8 8.7.18, `platforms/android-35/android.jar` (platform-35_r02),
kotlin-compiler 2.3.0 from Maven Central, apksigner + zipalign from apt.
```sh
AAPT2=/path/aapt2 ANDROID_JAR=/path/android-35/android.jar D8_JAR=/path/r8.jar \
KOTLINC_CP=/path/kotlin-compiler-2.3.0.jar:/path/kotlin-stdlib-2.3.0.jar:... \
sh build/build_linux.sh          # -> build/out/app-debug.apk (~2 min)
```
D8 prints many "error occurred when parsing kotlin metadata" warnings (R8 8.7
predates Kotlin 2.3); they only concern metadata and do not affect the dex.

## Install & run
With Shizuku/Wireless-ADB shell:
```sh
pm install ccapp/app-debug.apk
am start -n canteenconnect.app/.MainActivity
```
Note: `pm` (system_server) cannot read the APK from shared storage paths; stage it to
`/data/local/tmp` from a root/shizuku context, or install via a file manager that
offers "Install". The APK was produced and signature-verified on this device.

## Release builds
Not configured (no release keystore) — the debug APK is the milestone artifact, as
specified in the brief ("do not claim a signed release APK").

## Troubleshooting
- `libstdc++ missing` from Gradle → use build.sh (intentional), don't fight jextractor.
- D8 OOM → `java -Xmx3000m -cp $PREFIX/share/java/d8.jar com.android.tools.r8.D8 …`
  (already in build.sh).
- dep 404s → `build/deps/` is a pinned cache; re-run `dl.sh` only if versions change.
