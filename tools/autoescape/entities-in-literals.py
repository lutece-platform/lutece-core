#!/usr/bin/env python3
"""
LUT-33153 — replaces the HTML entities written inside FreeMarker string literals by the characters
they stand for.

With HTML auto-escaping, a value printed by ${...} is escaped: a literal 'a&amp;b' passed to a macro
comes out as a&amp;amp;b, and the browser shows "&amp;" (a broken link, a visible "&#8722;"). The
literal must hold the character itself; the escaping writes the entity.

    href='Page.jsp?a=1&amp;b=2'   ->   href='Page.jsp?a=1&b=2'
    label='&#8722;'               ->   label='−'

Only the literals inside FreeMarker tags (<#...>, <@...>) are rewritten. A literal holding a tag or
a directive is skipped: it is a template fragment, not a value.

Usage: entities-in-literals.py <templates-dir>... [--apply]
"""
import html
import os
import re
import sys

APPLY = '--apply' in sys.argv
ROOTS = [a for a in sys.argv[1:] if not a.startswith('--')]

TAG = re.compile(r"""<[#@](?:[^>'"]|'[^']*'|"[^"]*")*>""")
LITERAL = re.compile(r"""'([^'\\]*)'|"([^"\\]*)\"""")
ENTITY = re.compile(r'&(#\d+|#x[0-9a-fA-F]+|[a-zA-Z]+);')

count = 0
for root_dir in ROOTS:
    for root, _, files in os.walk(root_dir):
        for f in sorted(files):
            if not f.endswith(('.ftl', '.html')):
                continue
            path = os.path.join(root, f)
            src = open(path, encoding='utf-8', newline='').read()

            def literal(m):
                global count
                text = m.group(1) if m.group(1) is not None else m.group(2)
                quote = "'" if m.group(1) is not None else '"'
                if not ENTITY.search(text) or re.search(r'<[#@/a-zA-Z]', text):
                    return m.group(0)
                decoded = ENTITY.sub(lambda e: html.unescape(e.group(0)), text)
                if quote in decoded:
                    return m.group(0)
                count += 1
                print('  %s : %s  ->  %s' % (path, text[:70], decoded[:70]))
                return quote + decoded + quote

            new = TAG.sub(lambda t: LITERAL.sub(literal, t.group(0)), src)
            if new != src and APPLY:
                open(path, 'w', encoding='utf-8', newline='').write(new)
print('%d literal(s)' % count)
