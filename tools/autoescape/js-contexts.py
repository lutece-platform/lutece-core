#!/usr/bin/env python3
"""
LUT-33153 — lists every FreeMarker interpolation that lands in a JavaScript context: inside a
<script> element or an on* event attribute. HTML escaping is not JavaScript escaping:

  * inside <script>, the browser does not decode entities: ${x} written &#39; stays &#39;, and an
    ampersand of a URL becomes &amp;. A string value must be written ${x?js_string?no_esc}
    (JavaScript-escaped, not HTML-escaped). A number, a boolean or an id needs nothing.
  * inside an on* attribute, the browser decodes the HTML entities first, then runs the
    JavaScript: ${x?js_string} is right, auto-escaping adds the HTML layer on top.

Usage: js-contexts.py <templates-dir>   (prints file:line, context, interpolation)
"""
import os
import re
import sys

SCRIPT = re.compile(r'<script\b[^>]*>(.*?)</script>', re.S | re.I)
HANDLER = re.compile(r'\son[a-z]+\s*=\s*("[^"]*"|\'[^\']*\')', re.I)
INTERP = re.compile(r'\$\{((?:[^{}]|\{[^{}]*\})*)\}')

for root, _, files in os.walk(sys.argv[1]):
    for f in sorted(files):
        if not f.endswith(('.ftl', '.html')):
            continue
        path = os.path.join(root, f)
        src = re.sub(r'<#noparse>.*?</#noparse>', lambda m: re.sub(r'[^\n]', ' ', m.group(0)), open(path, encoding='utf-8').read(), flags=re.S)
        for kind, rx in (('script', SCRIPT), ('handler', HANDLER)):
            for m in rx.finditer(src):
                body = m.group(1)
                for i in INTERP.finditer(body):
                    line = src[:m.start(1) + i.start()].count('\n') + 1
                    before = body[:i.start()].split('\n')[-1]
                    quoted = (before.count("'") % 2 == 1) or (before.count('"') % 2 == 1) or (before.count('`') % 2 == 1)
                    print('%s:%d\t%s\t%s\t${%s}' % (path, line, kind, 'string' if quoted else 'code', i.group(1)))
