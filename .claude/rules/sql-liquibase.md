---
description: "Lutece 8 SQL: every plugin .sql (create_db, init_db, init_core, upgrade) MUST carry the Liquibase formatted-sql header, otherwise the schema silently fails to deploy in v8"
paths:
  - "**/sql/**/*.sql"
---

# SQL & Liquibase — Lutece 8

## The Golden Rule

In Lutece 8 the database is deployed by **plugin-liquibase**, which scans the classpath (`includeAll path="sql"`) and runs each plugin `.sql` as a **Liquibase formatted-SQL changeset**. A `.sql` file **without the Liquibase header is "not managed by liquibase"** and is silently skipped — and a single skipped/unparseable file aborts the whole plugin's migration.

**Every `.sql` file under `src/sql/plugins/<plugin>/` (create_db, init_db, init_core, AND every upgrade script) MUST start with this 3-line header:**

```sql
-- liquibase formatted sql
-- changeset <plugin>:<exact-file-name>.sql
-- preconditions onFail:MARK_RAN onError:WARN
```

Example (`update_db_appointment_3.0.8-4.0.0.sql`):

```sql
-- liquibase formatted sql
-- changeset appointment:update_db_appointment_3.0.8-4.0.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
CREATE TABLE IF NOT EXISTS appointment_slot_hold ( ... );
ALTER TABLE appointment_slot ADD CONSTRAINT chk_... CHECK ( ... );
```

## Rules

- **`changeset` author = plugin name** ; **id = the exact file name** (with `.sql`). Together with the file **path**, they form the identity Liquibase tracks in `DATABASECHANGELOG` — the triple `(ID, AUTHOR, FILENAME)`.
- **Never change the author, the id or the path of an already-shipped changeset** — they are the identity Liquibase tracks, and changing any of them replays it. One consequence: after a plugin rename, the changesets keep an author bearing the **former** plugin name — see `sql-rename.md`.
- **Editing the content of an already-shipped changeset is fatal as soon as the file is still part of the changelog for that run.** Being *included* is enough to be *validated*: Liquibase finds the row by its triple, compares `MD5SUM`, and raises `ValidationFailedException` before executing anything — the changeset is not replayed, it is refused. `TestIncludeAllFilter` excludes `create_*` / `init_*` files only once `core.plugins.status.<plugin>.version` is recorded, so on a plugin whose metadata is consistent the edit is invisible and reaches new installs only. That is the house practice and it holds — until a site has no version key: `No plugin metadata for <x>`, a datastore restored without those rows, or the first startup after a rename, where the key is written at the *end* of the run while the creation script is still included.
- The header is **mandatory on NEW files too** — when you add an upgrade script or edit `create_db`, match the header already present on the sibling files.
- Editing **inside** an existing changeset file needs **no new header** — it stays one changeset, which is exactly the trap above. To change what a creation script produces, **append a new changeset** to the same file: a checksum is computed per changeset, not per file, so the existing one keeps its identity and its fingerprint. Adding a **new file** always needs its own header.
- `IF NOT EXISTS` / idempotent DDL is good practice (re-run safety), but does **not** replace the header.
- **The third header line protects nothing on its own.** `-- preconditions onFail:MARK_RAN onError:WARN` declares *how* to react to a precondition, not a precondition. Without an actual check below it, the changeset runs unconditionally. To make a script genuinely re-run safe, add a real one:

  ```sql
  -- preconditions onFail:MARK_RAN onError:WARN
  -- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema=database() AND table_name='my_table' AND column_name='my_column'
  ALTER TABLE my_table ADD COLUMN my_column SMALLINT;
  ```

  Keep the policy line everywhere for consistency, but do not read it as a safety net — most scripts of the estate carry it with no condition attached, which is why replaying a non-idempotent `init_*` breaks the startup instead of being marked as ran.
- Don't declare `ON DELETE CASCADE` to clean child tables — the house convention is a **restrictive FK + explicit `deleteByIdForm`/`deleteByIdSlot`** chained in the service (see `FormService.removeForm`).

## The failure mode is the whole webapp

`db/changelog.xml` holds a single `<includeAll>` for the entire webapp, so every plugin is deployed by one `update()` call: one refused changeset fails all of them. `LiquibaseRunnerContext.setComponentVersion` only appends to an in-memory list, and `LiquibaseRunnerContext.close()` — which writes every `core.plugins.status.<plugin>.version` through `DatastoreService.setDataValue` — is reached only after a successful `update()`. A failed startup therefore records no version key at all, `LiquibaseRunner` rethrows a `RuntimeException` and `isCriticalService()` returns `true`: **Lutece does not start**, and this is not scoped to the guilty plugin.

The next startup reads the same inputs and fails identically. It cannot heal itself, since the mechanism that would have recorded the version is the one that failed. Unblocking means fixing `MD5SUM` by hand or running `clearCheckSums` — on every site.

## Changing what a creation script produces

Append a changeset instead of editing the shipped one:

```sql
-- changeset <plugin>:create_db_<plugin>-rev1.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = database() AND table_name = 'my_table' AND column_name = 'my_column'
ALTER TABLE my_table ADD COLUMN my_column VARCHAR(350);
```

- id = the file name carrying a `-rev<n>` suffix, the convention already in use in the estate.
- **no `logicalFilePath`**, even in a renamed directory: a new changeset has no row to match and must register under its real path.
- author = the **current** plugin name, while the changesets around it keep the former one.
- a **real** precondition, scoped on the current schema through `database()`. On a server hosting several Lutece schemas, an unscoped `information_schema` count sees a sibling schema and would make a fresh install skip the `ALTER`.

It holds in the three branches of the filter: executed on an empty database, `MARK_RAN` on a site that already got the column from an `update_*` script, no effect on a second pass.

`-- validCheckSum: ANY` also silences a mismatch — on its own line, the attribute is ignored on the `changeset` line. But Liquibase does **not** rewrite the stored `MD5SUM`, so the directive can never be removed afterwards, and it disables the integrity check on scripts that usually begin with `DROP TABLE IF EXISTS`. Permanent debt, on the least forgiving file of the plugin.

## Why it bites only in v8

v7 installs SQL via the Ant `build.xml` (runs every plugin `.sql` regardless of header), so a missing header **works in v7 and on a fresh v7 base**. v8 (Liberty/cluster, `LIQUIBASE_ENABLED_AT_STARTUP`) deploys **only** Liquibase-managed changesets → the same file silently fails to create its tables.

## Common Error

| Symptom | Cause | Fix |
|---|---|---|
| Plugin tables missing after v8 startup, **no exception** in app log | A plugin `.sql` lacks the `-- liquibase formatted sql` header | Add the 3-line header to that file |
| Log: `LiquibaseRunner files not managed by liquibase are sql/plugins/<x>/...` | That exact file has no/invalid header | Add/fix the header |
| Works in v7 fresh install, not in v8 cluster | Relying on the Ant build instead of Liquibase changesets | Header every `.sql` |
| Creation script replayed on an existing site, `Duplicate entry` or `DROP TABLE` | A SQL directory was renamed, changing the changeset identity | `logicalFilePath` on the changeset line — see `sql-rename.md` |
| `ValidationFailedException`, `1 changesets check sum`, **and the webapp does not start** | The content of an already-shipped changeset was edited, and the file is still included — no `core.plugins.status.<plugin>.version` | Append a new changeset instead of editing. Sites already broken need `MD5SUM` fixed or `clearCheckSums` |

## How to verify

Every `.sql` under `src/sql` must have `-- liquibase formatted sql` as its first non-empty line:

```bash
for f in $(find src/sql -name '*.sql'); do head -1 "$f" | grep -q 'liquibase formatted sql' || echo "MISSING HEADER: $f"; done
```
