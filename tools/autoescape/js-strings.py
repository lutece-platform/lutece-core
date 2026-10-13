#!/usr/bin/env python3
"""
LUT-33153 — writes the interpolations that sit inside a JavaScript string literal of a <script>
element as ${x?js_string?no_esc}.

Inside <script> the browser does not decode HTML entities, so the HTML escaping of auto-escaping
corrupts the value (an apostrophe becomes &#39;, the ampersand of a URL &amp;) and does not
protect it (a backslash or a line break still breaks out). ?js_string is the escaping of that
context; ?no_esc stops the HTML escaping on top of it.

Left alone, and listed:
  * an interpolation already ending with ?no_esc, ?c or an ISO date built-in (no character to escape);
  * an interpolation inside a backtick template literal: ?js_string does not escape ` nor ${,
    so such a literal must be turned into a quoted string by hand;
  * an interpolation outside any string literal (JavaScript code built by the template).

Usage: js-strings.py <templates-dir>... [--apply]
"""
import os
import re
import sys

APPLY = '--apply' in sys.argv
ROOTS = [a for a in sys.argv[1:] if not a.startswith('--')]


FTL_TAG = re.compile(r'<[@#](?:[^>"\']|"[^"]*"|\'[^\']*\')*>')


def without_ftl_tags(body, rewrite):
    """applies rewrite to a script body whose FreeMarker tags are masked: a macro call or a directive sits
    in the script, but its parameters are FreeMarker strings, not JavaScript"""
    tags = []

    def mask(m):
        tags.append(m.group(0))
        return '\x01%d\x01' % (len(tags) - 1)

    return re.sub(r'\x01(\d+)\x01', lambda m: tags[int(m.group(1))], rewrite(FTL_TAG.sub(mask, body)))

SCRIPT = re.compile(r'(<script\b[^>]*>)(.*?)(</script>)', re.S | re.I)
INTERP = re.compile(r'\$\{((?:[^{}]|\{[^{}]*\})*)\}')
SAFE_END = re.compile(r'\?(no_esc|c|iso\w*|js_string\?no_esc)\s*$')


def quote_state(code):
    """the string delimiter open at the end of code, or None; JavaScript comments are skipped"""
    state = None
    i = 0
    while i < len(code):
        c = code[i]
        if state:
            if c == '\\':
                i += 2
                continue
            if c == state:
                state = None
        elif code.startswith('//', i) and (i == 0 or code[i - 1] != ':'):
            end = code.find('\n', i)
            i = len(code) if end < 0 else end
            continue
        elif code.startswith('/*', i):
            end = code.find('*/', i + 2)
            i = len(code) if end < 0 else end + 2
            continue
        elif c in '\'"`':
            state = c
        i += 1
    return state


report = []
count = 0
for root_dir in ROOTS:
    for root, _, files in os.walk(root_dir):
        for f in sorted(files):
            if not f.endswith(('.ftl', '.html')):
                continue
            path = os.path.join(root, f)
            src = open(path, encoding='utf-8', newline='').read()

            def script(m):
                global count
                if re.search(r'type\s*=\s*["\'](text/template|text/html|text/x-)', m.group(1), re.I):
                    return m.group(0)
                return m.group(1) + without_ftl_tags(m.group(2), rewrite) + m.group(3)

            def rewrite(body):
                global count
                out, pos = [], 0
                for i in INTERP.finditer(body):
                    expr = i.group(1).strip()
                    line = src[:src.find(body[max(0, i.start() - 40):i.end()])].count('\n') + 1 if body[max(0, i.start() - 40):i.end()] in src else 0
                    state = quote_state(body[:i.start()])
                    if SAFE_END.search(expr) or expr.startswith('navigator.'):
                        continue
                    if state == '`':
                        report.append('%s:%d backtick literal: ${%s}' % (path, line, expr))
                        continue
                    if state is None:
                        report.append('%s:%d JavaScript code: ${%s}' % (path, line, expr))
                        continue
                    if expr.endswith('?js_string'):
                        new = expr + '?no_esc'
                    elif re.fullmatch(r'[\w.\[\]\'"()]+', expr):
                        new = expr + '?js_string?no_esc'
                    else:
                        new = '( %s )?js_string?no_esc' % expr
                    out.append(body[pos:i.start()])
                    out.append('${%s}' % new)
                    pos = i.end()
                    count += 1
                out.append(body[pos:])
                return ''.join(out)

            # a <#noparse> block is not FreeMarker: its ${...} belong to JavaScript template literals. It is
            # masked (same length) while the scripts are rewritten, then put back
            blocks = []

            def mask(m):
                blocks.append(m.group(0))
                return '\x00%d\x00' % (len(blocks) - 1)

            masked = re.sub(r'<#noparse>.*?</#noparse>', mask, src, flags=re.S)
            new = re.sub(r'\x00(\d+)\x00', lambda m: blocks[int(m.group(1))], SCRIPT.sub(script, masked))
            if new != src and APPLY:
                open(path, 'w', encoding='utf-8', newline='').write(new)

print('%d interpolation(s) in JavaScript strings now ?js_string?no_esc' % count)
print('%d left for review:' % len(report))
for r in report:
    print('  ' + r)
