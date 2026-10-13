#!/usr/bin/env bash
#
# LUT-33387 — every macro that declares attrs must write it: each one is called with
# attrs={'data-attrs-probe': 'p"<&'} (plus its required arguments) and must output
# data-attrs-probe exactly once. Run on the working tree, in both escaping modes.
#
# Usage: ./check-attrs.sh

set -euo pipefail

HERE="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT="$( cd "${HERE}/../../../.." && pwd )"
FREEMARKER_JAR="${FREEMARKER_JAR:-${HOME}/.m2/repository/org/freemarker/freemarker/2.3.34/freemarker-2.3.34.jar}"
WORK="${HERE}/../snapshots/macros"
mkdir -p "${WORK}/classes"
javac -d "${WORK}/classes" -cp "${FREEMARKER_JAR}" "${HERE}/MacroHarness.java"

python3 "${HERE}/gen-calls.py" "${PROJECT}/webapp/WEB-INF/templates" --attrs-probe > "${WORK}/calls-attrs.jsonl"
mkdir -p "${WORK}/attrs-true"
find "${WORK}/attrs-true" -mindepth 1 -delete
java -cp "${WORK}/classes:${FREEMARKER_JAR}" MacroHarness "${PROJECT}/webapp/WEB-INF/templates" "${WORK}/calls-attrs.jsonl" "${WORK}/attrs-true" true

python3 - "${WORK}/attrs-true" <<'PY'
import os, re, sys
out = sys.argv[1]
# macros that write their element only with data the probe call does not give them
CONDITIONAL = {
    "cInputGroupAddon": "writes the addon only with addonText",
    "cMainBanner": "writes the banner only on the home page or when the site properties ask for it",
    "cSlide": "without a slide, writes a placeholder card",
}
missing, errors, ok = [], [], 0
for f in sorted(os.listdir(out)):
    text = open(os.path.join(out, f), encoding="utf-8").read()
    body = text.split("\n", 2)[2] if text.count("\n") >= 2 else ""
    if f[:-4] in CONDITIONAL:
        continue
    if body.lstrip().startswith("!!"):
        errors.append("%s: %s" % (f[:-4], body.strip()[:160]))
    elif len(re.findall(r'data-attrs-probe="', body)) == 1:
        ok += 1
    else:
        missing.append("%s (%d)" % (f[:-4], len(re.findall(r'data-attrs-probe="', body))))
print("%d macro(s) write attrs" % ok)
print("%d cannot be rendered outside a request (not checked):" % len(errors))
for e in errors:
    print("    " + e)
print("%d declare attrs but do not write it exactly once:" % len(missing))
for m in missing:
    print("    " + m)
sys.exit(1 if missing else 0)
PY
