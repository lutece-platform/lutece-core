#!/usr/bin/env bash
#
# LUT-33153 — runs the end-to-end suite twice against the same code base, once with
# service.freemarker.templateAutoEscape=false and once with =true, then diffs the two sets
# of normalised page snapshots.
#
# An empty diff is the proof we are after: the migrated templates render identically in
# both modes, so the property can be flipped on any instance without a visual regression.
#
# Usage:  ./run-both-modes.sh
#
# Requirements: a MariaDB reachable by the configured datasource, and playwright-core
# resolvable through NODE_PATH (see PLAYWRIGHT_NODE_PATH below).

set -uo pipefail

HERE="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT="$( cd "${HERE}/../../.." && pwd )"
CONF="${PROJECT}/webapp/WEB-INF/conf/config.properties"
LOG="${HERE}/liberty.log"

BASE_URL="${BASE_URL:-http://localhost:9080/lutece-core}"
CONFIG_DIRECTORY="${CONFIG_DIRECTORY:-/home/yahiaoui/lutece/openlibertyConfigFirectory/main/liberty/config}"
PLAYWRIGHT_NODE_PATH="${PLAYWRIGHT_NODE_PATH:-/usr/local/lib/node_modules/agent-browser/node_modules}"
PROPERTY="service.freemarker.templateAutoEscape"

say( ) { printf '\n=== %s ===\n' "$*"; }

stop_liberty( ) {
    pkill -f "liberty.*luteceServer" 2>/dev/null
    ( cd "${PROJECT}" && mvn -o -q liberty:stop > /dev/null 2>&1 )
    sleep 3
}

set_property( ) {
    local value="$1"
    # remove any previous occurrence, then append
    sed -i "/^${PROPERTY}=/d" "${CONF}"
    printf '\n# LUT-33153 e2e harness\n%s=%s\n' "${PROPERTY}" "${value}" >> "${CONF}"
    echo "  ${PROPERTY}=${value}"
}

start_liberty( ) {
    ( cd "${PROJECT}" && nohup env -u HTTP_PROXY -u HTTPS_PROXY -u http_proxy -u https_proxy \
        mvn -o liberty:dev -Dmaven.test.skip=true -DskipTests \
        -DconfigDirectory="${CONFIG_DIRECTORY}" > "${LOG}" 2>&1 < /dev/null & )

    echo -n "  waiting for ${BASE_URL} "
    for _ in $( seq 1 120 ); do
        if curl -sf --noproxy '*' -o /dev/null -m 5 "${BASE_URL}/jsp/admin/AdminLogin.jsp"; then
            echo " up"
            return 0
        fi
        echo -n '.'
        sleep 5
    done
    echo " TIMEOUT"
    tail -30 "${LOG}"
    return 1
}

run_suite( ) {
    local mode="$1"
    NODE_PATH="${PLAYWRIGHT_NODE_PATH}" BASE_URL="${BASE_URL}" MODE="${mode}" \
        node "${HERE}/autoescape-e2e.js"
}

# --------------------------------------------------------------------------------------

ORIGINAL_CONF="$( mktemp )"
cp "${CONF}" "${ORIGINAL_CONF}"
trap 'cp "${ORIGINAL_CONF}" "${CONF}"; rm -f "${ORIGINAL_CONF}"' EXIT

rm -rf "${HERE}/snapshots"
RC=0

for mode in false true; do
    say "mode ${PROPERTY}=${mode}"
    stop_liberty
    set_property "${mode}"
    start_liberty || exit 1
    run_suite "${mode}" || RC=1
done

stop_liberty

say "diff of the two modes"
if diff -rq "${HERE}/snapshots/false" "${HERE}/snapshots/true" > "${HERE}/snapshots/diff.txt" 2>&1; then
    echo "  identical — every page renders the same with auto-escaping on and off"
else
    echo "  DIFFERENCES:"
    sed 's/^/    /' "${HERE}/snapshots/diff.txt"
    echo
    echo "  per-page detail:"
    for f in "${HERE}/snapshots/false"/*.html; do
        n="$( basename "${f}" )"
        if ! diff -q "${f}" "${HERE}/snapshots/true/${n}" > /dev/null 2>&1; then
            echo "    --- ${n} ---"
            diff "${f}" "${HERE}/snapshots/true/${n}" | head -20 | sed 's/^/      /'
        fi
    done
    RC=1
fi

exit "${RC}"
