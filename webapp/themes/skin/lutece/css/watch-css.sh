#!/usr/bin/env bash
# Watch this theme's CSS tree and regenerate each .min.css on save.
#
#   - Normal xxx.css           -> xxx.min.css (simple minify)
#   - theme.css / theme.rtl.css -> minified with @import paths rewritten to .min.css
#   - xxx.min.css              -> ignored (never used as a source)
#
# Uses inotifywait (package: inotify-tools) when available for instant,
# event-driven rebuilds. Otherwise falls back to a dependency-free polling
# loop (checks mtimes every 2s). Stop with Ctrl-C.

set -u

CSS_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MINIFY="/home/laurent/dev/liberty-apps/.claude/skills/minify-theme-css/minify_css.py"

minify_one() {
  local f="$1"
  case "$f" in
    *.min.css) return 0 ;;                       # never minify a min file
    */theme.css|*/theme.rtl.css)
      python3 "$MINIFY" --rewrite-imports "$f" ;; # aggregators: rewrite imports
    *.css)
      python3 "$MINIFY" "$f" ;;
  esac
}

echo "Watching $CSS_DIR for .css changes (Ctrl-C to stop)"

if command -v inotifywait >/dev/null 2>&1; then
  echo "Mode: inotifywait (event-driven)"
  inotifywait -m -q -r -e close_write --format '%w%f' "$CSS_DIR" | while read -r f; do
    case "$f" in *.css) minify_one "$f" ;; esac
  done
else
  echo "Mode: polling every 2s (install 'inotify-tools' for instant rebuilds)"
  declare -A SEEN
  # Prime with current mtimes so we don't rebuild everything on startup.
  while IFS= read -r f; do
    SEEN["$f"]="$(stat -c %Y "$f" 2>/dev/null)"
  done < <(find "$CSS_DIR" -name '*.css' ! -name '*.min.css')

  while true; do
    while IFS= read -r f; do
      m="$(stat -c %Y "$f" 2>/dev/null)"
      if [ "${SEEN["$f"]:-}" != "$m" ]; then
        SEEN["$f"]="$m"
        minify_one "$f"
      fi
    done < <(find "$CSS_DIR" -name '*.css' ! -name '*.min.css')
    sleep 2
  done
fi
