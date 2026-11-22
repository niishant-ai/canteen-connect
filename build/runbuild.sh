#!/system/bin/sh
# Canteen Mitra — background-safe build wrapper (fixes "Stream stalled - no data received for 90s").
#
# Why this exists:
#   sh build.sh run in the FOREGROUND can sit silent for 90-120s during the
#   kotlinc/D8 stages, which trips the harness's 90s no-data window.
#
# How to use (agent workflow — NEVER run this in the foreground):
#   1. shell_background:  sh ccapp/build/runbuild.sh
#      -> returns in <1s with a job id; stream stays active
#   2. poll — STRICT: every foreground shell call must RETURN in <75s,
#      otherwise the harness's 90s stream-inactivity watchdog fires
#      ("Stream stalled - no data received for 90s"). So:
#        shell: sh ccapp/build/buildstatus.sh     # <1s snapshot, use repeatedly
#      NEVER: "sleep 90; tail log" (sleep makes the whole call ~90s -> stall)
#   3. done when buildstatus.sh shows BUILD_EXIT=0 and "APK OK: ..."
#   (Optionally: shell_background: sh ccapp/build/wait_build.sh to block in bg
#    until the wrapper finishes, then peek with buildstatus.sh.)
#
# Guards:
#   - refuses to start a second build while one is running (prevents OOM)
#   - all output goes to build/last_build.log, not the model stream
set -eu
W=/storage/emulated/0/Android/data/com.androidharness.app/files/workspace/ccapp
cd "$W/build"

if [ -f .build.pid ] && kill -0 "$(cat .build.pid)" 2>/dev/null; then
  echo "ANOTHER BUILD IS RUNNING (pid $(cat .build.pid)) - refusing to start a second one."
  exit 1
fi
echo $$ > .build.pid

: > last_build.log
echo "$(date +%H:%M:%S) build started" >> last_build.log

# heartbeat: report the most recent [stage] marker from the log every 15s
( while kill -0 $$ 2>/dev/null; do
    sleep 15
    kill -0 $$ 2>/dev/null || break
    stage=$(grep -oE '^\[[a-z0-9 ]+\]' last_build.log 2>/dev/null | tail -1)
    [ -n "$stage" ] || stage="[running]"
    echo "$(date +%H:%M:%S) still $stage" >> last_build.log
  done ) &
HB=$!

sh "$W/build/build.sh" >> last_build.log 2>&1
rc=$?
kill "$HB" 2>/dev/null || true
rm -f .build.pid
echo "BUILD_EXIT=$rc $(date +%H:%M:%S)" >> last_build.log
exit $rc
