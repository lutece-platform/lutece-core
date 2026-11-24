# Prism Live (vendored)

Editable code areas powered by Prism: https://github.com/PrismJS/live (MIT), commit d8e240028f52775d48a84b6245277197a328374a.
Bliss v2 (MIT): https://github.com/LeaVerou/bliss, branch v2, commit 4f38d6effbbf7084f83ed3b686e3f8222a23527a, in src/vendor/bliss.

Changes to upstream:
- src/util.js imports Bliss from ./vendor/bliss/ instead of https://v2.blissfuljs.com/src/, so nothing is loaded from outside the site;
- the ES modules end in .js instead of .mjs (src/prism-live.mjs -> src/prism-live.esm.js, src/languages/*.mjs -> *.js, and the two
  imports in src/prism-live.js and src/util.js), because servers such as Open Liberty serve .mjs as text/plain and browsers refuse it.
Prism (../prism.js) must be loaded first.
