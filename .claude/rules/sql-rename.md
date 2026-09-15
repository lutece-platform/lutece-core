---
description: "Renaming a SQL directory or a plugin: logicalFilePath goes on the changeset line (not the file header), or existing sites replay their creation scripts and lose data"
paths:
  - "**/sql/**/*.sql"
  - "**/WEB-INF/plugins/*.xml"
---

# Renaming — SQL directory & plugin name

## The Golden Rule

A Liquibase changeset is identified by the triple **`(ID, AUTHOR, FILENAME)`**, where `FILENAME` is the **full path**, not the file name. Renaming a directory changes that identity: Liquibase no longer finds the row in `DATABASECHANGELOG`, treats the changeset as new and **replays it**.

The `MD5SUM` does not protect you — it fingerprints the *content* and is only compared once the row has been found by its triple.

**So: renaming a directory REQUIRES `logicalFilePath` on every changeset that already existed, and it MUST be on the `changeset` line.**

```sql
-- liquibase formatted sql
-- changeset myluteceaccessrules:create_db_accessrules.sql logicalFilePath:sql/plugins/accessrules/plugin/create_db_accessrules.sql
-- preconditions onFail:MARK_RAN onError:WARN
```

The declared path is the **former** one, classpath-relative — it starts with `sql/`, never `src/`.

## Why not in the file header

Both positions are parsed, only one is honoured:

| liquibase | `-- liquibase formatted sql logicalFilePath:…` | `-- changeset a:b logicalFilePath:…` |
|---|---|---|
| 4.31.1 (v7 webapps) | honoured | honoured |
| **5.0.4 (v8 webapps)** | **ignored** for resources loaded through `includeAll` → replayed | honoured |

The changeset-line form is the only one valid on both lines of version.

## Rules

- **Only the files that existed before the rename** carry the directive. A script added afterwards has no row to match: it runs once under its new path and records itself there. Giving it a `logicalFilePath` would register it under a path that does not exist.
- **One directive per changeset, not per file.** A file declaring two changesets needs two directives, both pointing at the same logical path.
- **Never touch `author` or `id`** of an already-shipped changeset: changing the author replays it *despite* the directive, since the author is part of the triple.
- **The content is just as sensitive, for another reason.** Editing it raises `ValidationFailedException` as soon as the file is still included in the changelog — and a rename is precisely the situation where creation scripts *are* included, the version key being absent until the end of the first successful startup. So a content fix must not travel in the same release as the rename: ship it as an appended changeset, see `sql-liquibase.md`. The failure is not scoped to the plugin — the webapp does not start, and does not recover on its own.
- After a **plugin** rename, the changesets keep an author bearing the **former** plugin name, forever. That contradicts the "author = plugin name" convention of `sql-liquibase.md`, and it is the correct trade-off.

## Why the directory must follow a plugin rename

`SqlPathInfo.getFullPluginName()` builds the component name from the directory under `sql/plugins/`, plus the one under `modules/`. It must equal the plugin `<name>` exactly, otherwise:

```
LiquibaseRunner. No plugin metadata for accessrules
```

and `core.plugins.status.<name>.version` is **never written**. `TestIncludeAllFilter` then evaluates `alreadyInstalledVersion == null` at every startup, includes creation scripts only and **discards every `update_*` script** — the plugin's schema migrations can never run again.

## Renaming a plugin: the data migration

The plugin name is persisted outside the changelog, so no Liquibase mechanism can carry it: `core.plugins.status.<name>.installed`, `.pool`, `.version`, `.lastRunScriptType`, `plugins.uninstalled.<name>`, and the `plugin_name` columns of `core_admin_right`, `core_portlet_type`, `core_attribute`, `mylutece_attribute`, plus `genatt_entry_type.plugin`.

It **cannot** ship as an `upgrade_` script: at the startup that brings the rename, the version key does not exist yet under the new name, so the filter takes the `alreadyInstalledVersion == null` branch and includes creation scripts only. Ship it as an `init_` script with a **real** precondition:

```sql
-- liquibase formatted sql
-- changeset <newname>:init_core_<newname>-rename.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(DISTINCT 1) FROM core_datastore WHERE entity_key LIKE '%core.plugins.status.<oldname>.%'

DELETE FROM core_datastore WHERE entity_key LIKE '%core.plugins.status.<newname>.%';

UPDATE core_datastore
   SET entity_key = REPLACE( entity_key, 'core.plugins.status.<oldname>.', 'core.plugins.status.<newname>.' )
 WHERE entity_key LIKE '%core.plugins.status.<oldname>.%';

UPDATE core_admin_right SET plugin_name = '<newname>' WHERE plugin_name = '<oldname>';
```

**`LIKE` and `REPLACE`, never exact keys.** `DatastoreService.getInstanceKey` prefixes the key with the webapp instance name whenever the instance is not the default one, so a multi-instance deployment holds `NOTIFSTORE-02.core.plugins.status.<plugin>.installed`, one row per instance. Worse, the two families do not behave alike : `.installed` and `.pool` go through `setInstanceDataValue` and **are** prefixed, while `.version` and `.lastRunScriptType` are written by `LiquibaseRunnerContext` through `setDataValue` and are **not**. Exact-key statements silently miss every named instance, and an exact-key precondition returns 0, which marks the changeset as ran without migrating anything. `REPLACE` on the key covers both forms and every instance in one statement.

`COUNT(DISTINCT 1)` is used rather than `COUNT(*)` so the precondition answers 1 or 0 whatever the number of instances — an exact count could not be predicted. Checked on MariaDB and HSQLDB.

Also align the `PLUGIN_NAME` constant and every `PluginService.getPlugin("…")`: a diverging constant returns `null`, and `DAOUtil.loadPlugin(null)` silently falls back to the default connection service.

**Not affected by a plugin rename:** i18n keys (the bundle is computed from the *Java package*), daemons (keyed by `daemon-id`), RBAC resource types.

## Common Error

| Symptom | Cause | Fix |
|---|---|---|
| `Duplicate entry … for key 'PRIMARY'` at startup, migration aborted | creation script replayed after a directory rename | `logicalFilePath` on the changeset line |
| Tables emptied / dropped after an upgrade | same, on a `create_db_*` starting with `DROP TABLE` | same |
| Directive present but changeset still replayed | directive in the file header, on liquibase 5 | move it to the `changeset` line |
| `ValidationFailedException`, `1 changesets check sum`, webapp down | content of an already-shipped changeset was edited while the file was still included | append a new changeset rather than editing — see `sql-liquibase.md` |
| Module shows up as **not installed** after a plugin rename | `core.plugins.status.<old>.installed` not migrated | `init_` migration script above |
| `No plugin metadata for <x>` | SQL directory name ≠ plugin `<name>` | rename the directory, apply this rule |

## How to verify

Before renaming, record what the databases actually store — the `logicalFilePath` must match to the character:

```bash
mysql <db> -e "SELECT FILENAME FROM DATABASECHANGELOG WHERE FILENAME LIKE '%<old-directory>%';"
```

Every changeset of a renamed directory must carry a directive:

```bash
for f in $(find src/sql -name '*.sql'); do
  echo "$(basename $f) : $(grep -c '^-- changeset' $f) changeset(s), $(grep -c 'logicalFilePath:' $f) directive(s)"
done
```

The two counts must match on every file of a renamed directory.

After the first startup: no more `No plugin metadata`, the `.version` key present, and the former changesets reported as `Previously run` — not `Run`.

## Note on `-- preconditions onFail:MARK_RAN onError:WARN`

That line alone **protects nothing**: it declares a policy with no condition attached. It is present at the top of most scripts of the estate, where it gives a false impression of re-run safety. Only an actual `precondition-sql-check` (or equivalent) has an effect — as in the migration script above.
