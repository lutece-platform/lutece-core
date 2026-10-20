#!/usr/bin/env bash
#
# LUT-33387 — renders every macro of lutece-core with a set of argument variants, on a reference
# revision (develop by default) and on the working tree, and compares the two renderings call by
# call. The calls are generated from the reference revision, so both sides receive exactly the
# same arguments.
#
# Usage:   ./run-macros.sh [reference-revision] [reference autoescape] [current autoescape]
#          ./run-macros.sh develop false true   compares develop as it runs today with the working
#                                                tree under auto-escaping (LUT-33153)
# Requires a JDK and the freemarker jar of the local Maven repository (FREEMARKER_JAR).

set -euo pipefail

HERE="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT="$( cd "${HERE}/../../../.." && pwd )"
REF="${1:-develop}"
REF_MODE="${2:-false}"
CUR_MODE="${3:-${REF_MODE}}"
FREEMARKER_JAR="${FREEMARKER_JAR:-${HOME}/.m2/repository/org/freemarker/freemarker/2.3.34/freemarker-2.3.34.jar}"
# The htmlAttributes function of the core (LUT-33387) is shared when the core is compiled: its classes and the
# commons-text and commons-lang3 jars of the local Maven repository are added to the class path
CORE_CLASSES="${PROJECT}/target/classes"
COMMONS_JARS="$( ls "${HOME}"/.m2/repository/org/apache/commons/commons-text/*/commons-text-*.jar "${HOME}"/.m2/repository/org/apache/commons/commons-lang3/*/commons-lang3-*.jar 2>/dev/null | grep -v sources | sort -V | awk -F/ '{ jar[$(NF-2)] = $0 } END { for ( k in jar ) printf "%s:", jar[k] }' )"
WORK="${HERE}/../snapshots/macros"

mkdir -p "${WORK}/reference" "${WORK}/classes"
find "${WORK}/reference" -mindepth 1 -delete
git -C "${PROJECT}" archive "${REF}" webapp/WEB-INF/templates | tar -x -C "${WORK}/reference"

python3 "${HERE}/gen-calls.py" "${WORK}/reference/webapp/WEB-INF/templates" > "${WORK}/calls.jsonl"
python3 "${HERE}/params-to-map.py" "${WORK}/calls.jsonl" > "${WORK}/calls-map.jsonl"
javac -d "${WORK}/classes" -cp "${FREEMARKER_JAR}" "${HERE}/MacroHarness.java"

for side in reference current; do
    templates="${WORK}/reference/webapp/WEB-INF/templates"
    mode="${REF_MODE}"
    calls="${WORK}/calls.jsonl"
    if [ "${side}" = current ]; then
        templates="${PROJECT}/webapp/WEB-INF/templates"
        mode="${CUR_MODE}"
        calls="${WORK}/calls-map.jsonl"
    fi
    mkdir -p "${WORK}/${side}-${mode}"
    find "${WORK}/${side}-${mode}" -mindepth 1 -delete
    java -cp "${WORK}/classes:${FREEMARKER_JAR}:${CORE_CLASSES}:${COMMONS_JARS}" MacroHarness "${templates}" "${calls}" "${WORK}/${side}-${mode}" "${mode}"
done

python3 "${HERE}/compare-macros.py" "${WORK}/reference-${REF_MODE}" "${WORK}/current-${CUR_MODE}"
