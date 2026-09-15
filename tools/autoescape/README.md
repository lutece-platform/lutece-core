# Migrating a plugin to FreeMarker auto-escaping — LUT-33153

`service.freemarker.templateAutoEscape` switches `AbstractFreeMarkerTemplateService` to
`HTMLOutputFormat` + `ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY`. It defaults to `false`, and it is a
runtime property: **a template must work with both values**, because an instance that has not
switched still has to run.

These scripts and the accompanying tests are what was used to migrate `lutece-core`. They are meant
to be reused as-is on plugins.

## The one rule

> A value that carries HTML must stay *markup* from end to end. As soon as it goes back through a
> `String`, it will be escaped.
>
> Block captures propagate markup. String literals break it.

Everything below follows from that.

## What changes when the property is `true`

A block capture — `<#assign x>…</#assign>` — no longer produces a `String` but a *markup output*
value, tagged "already safe HTML". Two opposite consequences, and they are the two families of bugs:

| | symptom | fix |
|---|---|---|
| a markup value used as a string | `Can't compare values of these types`, `NonStringException` on `?trim` / `?boolean` / `#include` | `?has_content`, or normalize the capture |
| a `String` that holds HTML | markup displayed as text, `title=&quot;x&quot;` | keep it markup: capture, or `HtmlMarkup` on the Java side |

## The bi-compatible vocabulary

Verified identical under both settings by `FreeMarkerAutoEscapeCompatibilityTest`.

| Goal | Do not use | Use |
|---|---|---|
| Do not escape | `${x?no_esc}` | `<#noautoesc>${x}</#noautoesc>` |
| Escape explicitly | `${x?esc}`, `${x?html}` | `<#outputformat "HTML">${x}</#outputformat>` |
| Test a captured block | `c != ''`, `c = ''` | `c?has_content`, `!c?has_content` |
| Turn a capture back into a String | `?markup_string` alone | `x?is_markup_output?then(x?markup_string, x)` |
| Pass a bundle of attributes | `<#assign p = 'title="x"'>` | `<#assign p>title="x"</#assign>` |
| Print HTML built in Java | `${x?no_esc}` | `HtmlMarkup.of( html )` on the model, plain `${x}` in the template |
| Interpolate inside a `<script>` | `${x}`, `${x?js_string}` alone | `<#noautoesc>${x?js_string}</#noautoesc>` |

The JavaScript row is the one people get wrong. HTML escaping is not JavaScript escaping: with
auto-escaping on, `var m = '${x}'` turns an apostrophe into `&#39;` inside a JS string literal, and
`?js_string` on its own is then HTML-escaped on top. `<#noautoesc>` around `?js_string` is the only
form that produces correct JavaScript under both settings.

`<#noautoesc>` and `<#outputformat>` are *directives*, not output-format-bound built-ins: they parse
under both settings. `?no_esc` and `?esc` are a **ParseException** when the property is `false` — the
template does not load at all — and `?html` / `?xhtml` are a ParseException when it is `true`.

## The scripts

Run them in order, from the plugin root. Each takes the template root and, without `--apply`, only
reports.

```bash
CORE=/path/to/lutece-core/tools/autoescape
T=webapp/WEB-INF/templates

python3 $CORE/02-hoist-legacy-escape-in-macro-calls.py $T --apply
python3 $CORE/01-comparisons-normalize-legacy-escape.py $T --apply
python3 $CORE/03-single-equals-comparisons.py          $T --apply
python3 $CORE/04-text-captures.py                      $T --apply --report
python3 $CORE/05-html-entities-in-macro-args.py        $T --apply
python3 $CORE/06-attribute-bundles-in-macro-args.py    $T --apply
```

| script | what it does |
|---|---|
| `01` | `x != ''` → `x?has_content`; normalizes macro parameters that undergo a string operation; converts `${x?html}` in plain output to an `<#outputformat "HTML">` block |
| `02` | hoists `?html` / `?xhtml` that sit inside a macro call — where a block directive cannot go — into a capture |
| `03` | `x = ''` → `!x?has_content`, strictly inside `<#if>` / `<#elseif>` so a JavaScript assignment is never touched |
| `04` | normalizes captures whose body carries no markup (flags, labels, paths). Captures that really hold HTML are left alone |
| `05` | hoists hand-written HTML entities (`&amp;`) from macro arguments, so they are not escaped a second time |
| `06` | hoists literal attribute bundles (`params='aria-hidden="true"'`) into captures — the most frequent case, 189 sites in core |

**The trap to know about.** A blind rewrite of `x != ''` also catches JavaScript comparisons inside
`<script>` blocks, and silently breaks the page. Script `03` is bounded to FreeMarker tags for that
reason, and `01` has to be checked afterwards — which is what
`FreeMarkerAutoEscapeCompatibilityTest.noBuiltInLeakedOutsideADirective` does. It found 7 such cases
in core.

## What the scripts cannot do

HTML built on the Java side. There is no way to tell from a template whether `${content}` holds
markup or text. Look for the model keys a template prints raw, and wrap them:

```java
model.put( MARK_CONTENT, HtmlMarkup.of( strHtml ) );          // markup the application built
model.put( MARK_CONTENT, HtmlMarkup.ofSanitized( strHtml ) ); // markup coming from a user
```

For rich content, `RichTextContentService.getSafeContent( content )` converts, sanitizes and returns
the markup value in one call.

For a bean whose getter returns HTML, add a `getMarkup()` next to it rather than changing the
existing getter — see `AdminUserMenuItem`.

## Checking the result

Two levels, both reusable in a plugin.

**Unit** — copy `src/test/java/fr/paris/lutece/portal/service/template/FreeMarkerAutoEscapeCompatibilityTest.java`.
It parses every template of the module under both configurations and fails the build on `?no_esc`,
`?esc`, `?html`, `?xhtml`, and on a built-in that leaked into JavaScript. No container, no database.

**End to end** — `src/test/e2e/run-both-modes.sh` starts the application twice, once per value of the
property, walks the front office and every back-office page reachable from the menu, and diffs the
normalised page snapshots. An empty diff is the proof that the property can be flipped without a
visual regression.

## Order of work

1. `lutece-core` first, as a *compatibility* pass — not a migration. That is this commit: the macros
   accept both a `String` and a markup value, so a plugin can adopt the capture idiom without hitting
   `params != ''`.
2. Then the plugins, script + manual C5 pass, one pull request each.
3. The global property last, once the estate is bi-compatible.

Do **not** put `<#ftl output_format="HTML" auto_esc=true>` headers in plugin templates as a migration
vehicle. The header makes a single file behave the same under both settings, which is useful for a
handful of files; used at scale it creates a long-lived hybrid state where a non-migrated caller
feeding a migrated macro produces double escaping that no test catches.
