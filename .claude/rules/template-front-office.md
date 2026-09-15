---
description: "Lutece 8 front-office (skin/site) templates: Bootstrap 5, vanilla JS, core modules"
paths:
  - "**/templates/skin/**/*.html"
---

# Front-Office Templates — Lutece 8

## Already Loaded by the Core — No Import Needed

Bootstrap 5 (CSS + JS), Tabler Icons and core JS modules are globally loaded by the site frameset. Do NOT add `<link>`, `<script>` or `import` tags for these resources.

## CSS — Bootstrap 5 Only

Use exclusively Bootstrap 5 utility classes and components. No custom CSS unless strictly necessary. Refer to BS5 docs for class names (`container`, `row`, `col-*`, `card`, `btn`, `form-control`, `d-flex`, `gap-*`, etc.).

## JavaScript — Vanilla Only, No jQuery

- **NEVER** use jQuery (`$`, `jQuery`, `$.ajax`, `.click()`, `.on()`, etc.)
- Use native DOM APIs: `document.querySelector`, `addEventListener`, `fetch`, `classList`, `dataset`
- Use ES6+: `const`/`let`, arrow functions, template literals, destructuring, `async`/`await`

## Third-Party Libraries — No CDN

- **NEVER** use CDN links. All third-party JS/CSS must be downloaded and placed locally.
- JS files go in `webapp/js/{pluginName}/`
- CSS files go in `webapp/css/{pluginName}/`

## Common Patterns

- Icons: use Tabler icon classes (`ti ti-*`)
- Null safety in Freemarker: always `${value!}`
- i18n: `#i18n{prefix.key}` — reuse `portal.util.*` keys `from ~/.lutece-references/lutece-core` when possible

## Model Variables Null-Safety (MANDATORY)

`errors`, `infos`, `warnings` are **NOT pre-initialized** in the model — they only exist after `addError()`/`addInfo()`/`addWarning()` is called. Always use null-safe access:
- `<#if (errors!)?size gt 0>` NOT `<#if errors?size gt 0>`
- `<#list (errors![]) as error>` NOT `<#list errors as error>`
- Same for `infos` and `warnings`
