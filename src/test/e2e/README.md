# Template regression harness

Two complementary checks that the FreeMarker templates of lutece-core render the same pages before
and after a change (written for LUT-33387, refactoring of the macros).

## 1. Macro by macro — `macros/run-macros.sh`

Renders every macro declared under `webapp/WEB-INF/templates` with a set of argument variants
(required arguments only, every string argument set, each boolean and list argument flipped, special
characters in `class` and `title`), on a reference revision and on the working tree, with the
configuration `AbstractFreeMarkerTemplateService` builds. The two renderings are compared call by
call, ignoring the order of the attributes and insignificant white space.

    src/test/e2e/macros/run-macros.sh [reference-revision=develop] [autoescape=false|true]

Each macro whose body compares a string parameter with literals (`type='textarea'`,
`style?contains('modal')`) is also called once per literal. A difference made on purpose (a defect
fixed) is listed with its reason in `macros/accepted-diffs.txt`: it is printed, not failed.

    src/test/e2e/macros/check-attrs.sh

Calls every macro that declares `attrs` with a probe attribute and checks that it writes it exactly
once, so an attrs that a macro would silently drop cannot go unnoticed.

No server is needed. It covers macros no page of the core reaches.

## 2. Page by page — `pages-e2e.js` and `compare-snapshots.py`

Drives Chromium (playwright-core) against a running instance: the front office pages of the core,
then a crawl of every read-only back-office screen reachable from the admin menu (three levels).
Each page must not be an error page, must not show markup or a FreeMarker construct as text, and its
DOM must not contain a malformed attribute. Each raw server response is saved under
`snapshots/<LABEL>/`.

    # instance on the reference revision
    NODE_PATH=<dir containing playwright-core> LABEL=before node src/test/e2e/pages-e2e.js
    # instance on the working tree (templates are reloaded by liberty:dev)
    NODE_PATH=<dir containing playwright-core> LABEL=after node src/test/e2e/pages-e2e.js
    python3 src/test/e2e/compare-snapshots.py src/test/e2e/snapshots/before src/test/e2e/snapshots/after

Environment: `BASE_URL` (default `http://localhost:9080/lutece-core`), `ADMIN_USER` / `ADMIN_PASSWORD`
(default `admin` / `adminadmin`), `CHROMIUM` (browser executable, default: the one of playwright-core
or the newest headless shell in `~/.cache/ms-playwright`).

A database created from `src/sql/create_db_lutece_core.sql` and `src/sql/init_db_lutece_core.sql`
gives a known state; the `admin` account must not be asked to change its password
(`core_admin_user.reset_password = 0`).

### Against a site

The same crawl validates the plugins of a site: serve an exploded copy of the site whose
`WEB-INF/templates` is overlaid with the core templates of the reference revision
(`git archive develop webapp/WEB-INF/templates | tar -x -C <copy>`), crawl with `BASE_URL` pointing at
it and `LABEL=site-before`, overlay the templates of the working tree, crawl again with
`LABEL=site-after`, and compare. The application state (last login date, daemon logs) differs
between two crawls of a fresh database: crawl twice to tell it from a rendering change.
