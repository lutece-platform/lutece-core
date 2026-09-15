---
description: "When a task involves a dependency (Lutece or external), ensure its source/docs are available for exploration"
---

# Dependency References — Mandatory Source Lookup

When a user's request (feature, bug, question) involves — explicitly or implicitly — a dependency, you MUST have its source code or documentation before proceeding.

## Lutece Dependencies — Source Lookup

1. **Read `pom.xml`** — Identify the relevant dependency (artifactId, groupId)
2. **Check `~/.lutece-references/`** — If a directory matching the repo name already exists, use it
3. **If not found, search GitHub** — Search both orgs: `lutece-platform` and `lutece-secteur-public`
   ```bash
   curl -s "https://api.github.com/search/repositories?q=org:lutece-platform+{keyword}+in:name&per_page=5" | jq -r '.items[] | .name'
   curl -s "https://api.github.com/search/repositories?q=org:lutece-secteur-public+{keyword}+in:name&per_page=5" | jq -r '.items[] | .name'
   ```
4. **Find the v8 branch** — Check branches in priority order: `develop_core8` > `develop8` > `develop8.x` > `develop`
   ```bash
   curl -s "https://api.github.com/repos/{org}/{repo}/branches?per_page=100" | jq -r '.[].name'
   ```
5. **Verify v8 compatibility** — Fetch the remote pom.xml and check `<parent><version>` starts with `8.` (e.g. `8.0.0`, `8.0.0-SNAPSHOT`, `8.1.0`)
   ```bash
   curl -s "https://raw.githubusercontent.com/{org}/{repo}/{branch}/pom.xml" | grep -A1 '<parent>' | grep '<version>'
   ```
6. **Clone into references** — Use the same pattern as the SessionStart hook
   ```bash
   git clone -q --branch {branch} --single-branch https://github.com/{org}/{repo}.git ~/.lutece-references/{repo}
   ```

## When It Fails

- **No v8 branch found** (parent version is 7.x or no develop_core8/develop8 branch) → Warn the user: this dependency has no Lutece 8 version yet and needs to be migrated first
- **Repo not found on GitHub** → Ask the user to clone the repo manually and provide the local path for exploration

## External Dependencies (non-Lutece)

For external libraries (e.g. Apache Commons, Jackson, third-party APIs):

1. **Check if Context7 MCP is available** — Look for the `context7` tool in your available tools
2. **If Context7 is available** → Use it to fetch up-to-date documentation and source references for the library
3. **If Context7 is NOT available** → Inform the user:
   > The Context7 plugin can provide up-to-date documentation for external libraries. To install it, run `/plugin` and install the `context7` plugin, then restart the session with `claude -c`
4. **Fallback** — Use WebSearch/WebFetch to find official documentation, or ask the user for a local path to the library sources
