#!/system/bin/sh
# wait_build.sh — start this via shell_background, then poll the build log with
# FAST foreground calls only (each foreground call must return in < 75s).
# This script BLOCKS in the background until the build wrapper finishes, then
# writes a marker. It keeps the model stream alive because it runs as a bg job.
B=/storage/emulated/0/Android/data/com.androidharness.app/files/workspace/ccapp/build
echo "waiting for build completion..."
while :; do
  # build finished if the pid guard is gone AND a BUILD_EXIT line exists
  if [ ! -f "$B/.build.pid" ] && grep -q "BUILD_EXIT" "$B/last_build.log" 2>/dev/null; then
    grep "BUILD_EXIT" "$B/last_build.log" | tail -1
    echo "DONE $(date +%H:%M:%S)"
    exit 0
  fi
  sleep 15
done
