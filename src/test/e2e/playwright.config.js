// @ts-check
const { defineConfig, devices } = require( '@playwright/test' );

/**
 * LUT-33153 — end-to-end guard for the FreeMarker auto-escaping migration.
 *
 * The server is expected to be already running; the harness (run-both-modes.sh) starts it once per
 * value of service.freemarker.templateAutoEscape and runs this suite against each.
 *
 * Environment:
 *   BASE_URL        base URL of the running Lutece instance (default http://localhost:9080/lutece)
 *   ADMIN_USER      back-office login (default admin)
 *   ADMIN_PASSWORD  back-office password (default adminadmin)
 *   AUTOESCAPE      "true" | "false" — only used to name the snapshot directory
 */
module.exports = defineConfig( {
    testDir: './tests',
    timeout: 60_000,
    expect: { timeout: 10_000 },
    fullyParallel: false,
    workers: 1,
    retries: 0,
    reporter: [ [ 'list' ], [ 'json', { outputFile: `results-${process.env.AUTOESCAPE || 'unknown'}.json` } ] ],
    use: {
        baseURL: process.env.BASE_URL || 'http://localhost:9080/lutece',
        ignoreHTTPSErrors: true,
        screenshot: 'only-on-failure',
        trace: 'retain-on-failure',
        actionTimeout: 15_000,
    },
    projects: [ { name: 'chromium', use: { ...devices[ 'Desktop Chrome' ] } } ],
} );
