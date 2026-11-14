#!/system/bin/sh
# Fast build status snapshot — ALWAYS returns in <5s.
# Use this to peek at a background build started via runbuild.sh.
# Safe to call as a foreground shell command (well under the 90s stream window).
B=/storage/emulated/0/Android/data/com.androidharness.app/files/workspace/ccapp/build

if [ -f "$B/.build.pid" ] && kill -0 "$(cat "$B/.build.pid")" 2>/dev/null; then
  echo "BUILD RUNNING (pid $(cat "$B/.build.pid"))"
else
  echo "BUILD IDLE (last result below)"
fi
echo "--- last 6 log lines ---"
tail -n 6 "$B/last_build.log" 2>/dev/null
echo "--- result ---"
grep "BUILD_EXIT\|APK OK" "$B/last_build.log" 2>/dev/null | tail -2
ps -eo comm 2>/dev/null | grep -q "java" && echo "javac/java active" || true
