#!/usr/bin/env bash
#
# LUT-33387 — renders every macro of lutece-core with a set of argument variants, on a reference
# revision (develop by default) and on the working tree, and compares the two renderings call by
# call. The calls are generated from the reference revision, so both sides receive exactly the
# same arguments.
#
# Usage:   ./run-macros.sh [reference-revision] [autoescape: false|true]
# Requires a JDK and the freemarker jar of the local Maven repository (FREEMARKER_JAR).

set -euo pipefail

HERE="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT="$( cd "${HERE}/../../../.." && pwd )"
REF="${1:-develop}"
MODE="${2:-false}"
FREEMARKER_JAR="${FREEMARKER_JAR:-${HOME}/.m2/repository/org/freemarker/freemarker/2.3.34/freemarker-2.3.34.jar}"
WORK="${HERE}/../snapshots/macros"

mkdir -p "${WORK}/reference" "${WORK}/classes"
find "${WORK}/reference" -mindepth 1 -delete
git -C "${PROJECT}" archive "${REF}" webapp/WEB-INF/templates | tar -x -C "${WORK}/reference"

python3 "${HERE}/gen-calls.py" "${WORK}/reference/webapp/WEB-INF/templates" > "${WORK}/calls.jsonl"
javac -d "${WORK}/classes" -cp "${FREEMARKER_JAR}" "${HERE}/MacroHarness.java"

for side in reference current; do
    templates="${WORK}/reference/webapp/WEB-INF/templates"
    [ "${side}" = current ] && templates="${PROJECT}/webapp/WEB-INF/templates"
    mkdir -p "${WORK}/${side}-${MODE}"
    find "${WORK}/${side}-${MODE}" -mindepth 1 -delete
    java -cp "${WORK}/classes:${FREEMARKER_JAR}" MacroHarness "${templates}" "${WORK}/calls.jsonl" "${WORK}/${side}-${MODE}" "${MODE}"
done

python3 "${HERE}/compare-macros.py" "${WORK}/reference-${MODE}" "${WORK}/current-${MODE}"
