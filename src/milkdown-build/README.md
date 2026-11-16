# Milkdown bundle of the back office

`webapp/themes/admin/shared/js/editors/milkdown/milkdown.min.js` and `milkdown.min.css` are built from `entry.js` with the versions pinned in `package.json`
(`@milkdown/crepe` and `@milkdown/kit` 7.22.2, esbuild 0.28.2), and the versions of their dependencies locked in `package-lock.json`. Milkdown is published as ES modules with about twenty dependencies, and
the core has no npm build: the bundle is built by hand and committed, as the Tabler files are.

The bundle holds the Crepe builder and the features Lutece uses (block edit, toolbar, top bar, link tooltip, image block, list item,
placeholder, table, cursor). CodeMirror, LaTeX and AI are left out: nothing is fetched from a CDN and no KaTeX font is needed. It exposes
`window.LuteceMilkdownKit`, used by `lutece-milkdown.js` next to the bundle.

`lutece.css` replaces Crepe's `frame-dark.css`, which redefines every `.milkdown` and would make all the editors dark: its colours are
scoped to `[data-bs-theme="dark"]`, the editor takes the fonts of the admin theme (the Noto fonts of Crepe are not loaded) and its
height from `--lutece-milkdown-height`.

## Rebuild

From the root of lutece-core:

```bash
mkdir -p /tmp/milkdown-build && cp src/milkdown-build/entry.js src/milkdown-build/lutece.css src/milkdown-build/package.json src/milkdown-build/package-lock.json /tmp/milkdown-build/
OUT="$PWD/webapp/themes/admin/shared/js/editors/milkdown"
cd /tmp/milkdown-build
npm ci
npx esbuild entry.js --bundle --minify --format=iife --target=es2020 --loader:.svg=text --legal-comments=none \
    --metafile=meta.json --outfile="$OUT/milkdown.min.js"
```

`npm ci` installs exactly the locked versions, so the bundle comes out the same byte for byte. To move to newer versions, change
`package.json`, run `npm install` to update `package-lock.json`, and commit both with the new bundle.

esbuild writes the CSS next to the JS (`milkdown.min.css`). Then regenerate `THIRD-PARTY-NOTICES.txt`, which stays next to the bundle, from `meta.json`: one line
per bundled package (name, version, license), followed by the license file of each package.

These build inputs are kept out of `webapp` so that they are not copied into the war nor served by the sites. Build in a temporary
folder, so that `node_modules` never lands in the repository.
