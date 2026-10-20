# Milkdown bundle of the back office

`../milkdown.min.js` and `../milkdown.min.css` are built from `entry.js` with the versions pinned in `package.json`
(`@milkdown/crepe` and `@milkdown/kit` 7.22.2, esbuild 0.28.2). Milkdown is published as ES modules with about twenty dependencies, and
the core has no npm build: the bundle is built by hand and committed, as the Tabler files are.

The bundle holds the Crepe builder and the features Lutece uses (block edit, toolbar, top bar, link tooltip, image block, list item,
placeholder, table, cursor). CodeMirror, LaTeX and AI are left out: nothing is fetched from a CDN and no KaTeX font is needed. It exposes
`window.LuteceMilkdownKit`, used by `../lutece-milkdown.js`.

`lutece.css` replaces Crepe's `frame-dark.css`, which redefines every `.milkdown` and would make all the editors dark: its colours are
scoped to `[data-bs-theme="dark"]`, the editor takes the fonts of the admin theme (the Noto fonts of Crepe are not loaded) and its
height from `--lutece-milkdown-height`.

## Rebuild

```bash
mkdir -p /tmp/milkdown-build && cp entry.js lutece.css package.json /tmp/milkdown-build/ && cd /tmp/milkdown-build
npm install
npx esbuild entry.js --bundle --minify --format=iife --target=es2020 --loader:.svg=text --legal-comments=none \
    --metafile=meta.json --outfile=<this folder>/../milkdown.min.js
```

esbuild writes the CSS next to the JS (`milkdown.min.css`). Then regenerate `../THIRD-PARTY-NOTICES.txt` from `meta.json`: one line
per bundled package (name, version, license), followed by the license file of each package.

Build outside the webapp, so that `node_modules` never lands in it.
