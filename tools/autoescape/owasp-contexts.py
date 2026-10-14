#!/usr/bin/env python3
"""
LUT-33153 — lists the interpolations a template writes in a context where HTML escaping is not
enough, after the OWASP XSS Prevention Cheat Sheet ("dangerous contexts") and the DOM based XSS
Prevention Cheat Sheet:

  unquoted     an HTML attribute value without quotes: escaping does not stop a space or =
  handler      an event handler attribute (on...): code, only ?js_string in a quoted JS string is safe
  url-start    a URL attribute (href, src, action, formaction...) starting with a value: a
               javascript: or data: URL is not stopped by escaping, the scheme must be checked
  css          a style attribute or a <style> element: CSS is a dangerous context
  comment      an HTML comment
  dom-sink     a value or i18n text written by a script with innerHTML, outerHTML,
               insertAdjacentHTML, document.write, eval, setTimeout/setInterval with a string, or
               jQuery html()/append()/prepend()/before()/after()

Values that cannot carry markup are skipped: ?c, ?js_string inside quotes of a handler, numbers
built-ins, i18n keys. Usage: owasp-contexts.py <templates-dir>...
"""
import os
import re
import sys

INTERP = re.compile(r'\$\{((?:[^{}]|\{[^{}]*\})*)\}')
SAFE = re.compile(r'\?(c|then\(\s*\'[^\']*\'\s*,\s*\'[^\']*\'\s*\)|string\.computer|iso\w*|size|index|counter|has_content|url|url_path)\s*$')
URL_ATTRS = 'href|src|action|formaction|data|poster|background|cite|srcset|xlink:href'


def masked(src):
    src = re.sub(r'<#--.*?-->', lambda m: re.sub(r'[^\n]', ' ', m.group(0)), src, flags=re.S)
    return re.sub(r'<#noparse>.*?</#noparse>', lambda m: re.sub(r'[^\n]', ' ', m.group(0)), src, flags=re.S)


def html_tags(src):
    """HTML start tags (not FreeMarker ones), with their position"""
    for m in re.finditer(r'<([a-zA-Z][\w:-]*)((?:[^<>"\']|"[^"]*"|\'[^\']*\'|<#[^>]*>|</#[^>]*>|\$\{[^}]*\})*)>', src):
        yield m


report = []
for root_dir in sys.argv[1:]:
    for root, _, files in os.walk(root_dir):
        for f in sorted(files):
            if not f.endswith(('.ftl', '.html')):
                continue
            path = os.path.join(root, f)
            src = masked(open(path, encoding='utf-8', errors='replace').read())

            def add(kind, pos, text):
                report.append('%s:%d  %-9s %s' % (path, src[:pos].count('\n') + 1, kind, text.replace('\n', ' ')[:150]))

            for tag in html_tags(src):
                attrs = tag.group(2)
                base = tag.start(2)
                top = re.sub(r'"[^"]*"|\'[^\']*\'', lambda q: ' ' * len(q.group(0)), attrs)
                for a in re.finditer(r'(?<![\w:-])([\w:-]+)=(\$\{(?:[^{}]|\{[^{}]*\})*\})', top):
                    if not SAFE.search(a.group(2)[2:-1]):
                        add('unquoted', base + a.start(), a.group(0))
                for a in re.finditer(r'(?<![\w:-])(on[a-z]+)\s*=\s*("([^"]*)"|\'([^\']*)\')', attrs, re.I):
                    body = a.group(3) if a.group(3) is not None else a.group(4)
                    for i in INTERP.finditer(body):
                        e = i.group(1).strip()
                        if SAFE.search(e) or e.endswith('?js_string'):
                            continue
                        add('handler', base + a.start(), '%s: ${%s}' % (a.group(1), e))
                for a in re.finditer(r'(?<![\w:-])(%s)\s*=\s*["\']\s*(\$\{(?:[^{}]|\{[^{}]*\})*\})' % URL_ATTRS, attrs, re.I):
                    add('url-start', base + a.start(), a.group(0))
                for a in re.finditer(r'(?<![\w:-])style\s*=\s*("[^"]*\$\{[^"]*"|\'[^\']*\$\{[^\']*\')', attrs, re.I):
                    for i in INTERP.finditer(a.group(1)):
                        if not SAFE.search(i.group(1)):
                            add('css', base + a.start(), 'style: ${%s}' % i.group(1).strip())
            for m in re.finditer(r'<style\b[^>]*>(.*?)</style>', src, re.S | re.I):
                for i in INTERP.finditer(m.group(1)):
                    if not SAFE.search(i.group(1)):
                        add('css', m.start(1) + i.start(), '<style>: ${%s}' % i.group(1).strip())
            for m in re.finditer(r'<!--(.*?)-->', src, re.S):
                for i in INTERP.finditer(m.group(1)):
                    add('comment', m.start(1) + i.start(), '${%s}' % i.group(1).strip())
            for m in re.finditer(r'<script\b[^>]*>(.*?)</script>', src, re.S | re.I):
                body = m.group(1)
                for s in re.finditer(r'(\.innerHTML\s*[+]?=|\.outerHTML\s*=|insertAdjacentHTML\s*\(|document\.write(?:ln)?\s*\(|\beval\s*\(|set(?:Timeout|Interval)\s*\(\s*["\']|\.(?:html|append|prepend|before|after)\s*\()([^;\n]*)', body):
                    rest = s.group(2)
                    if re.match(r'\.(html|append|prepend|before|after)', s.group(1)) and '<' not in rest:
                        continue
                    if INTERP.search(rest) or '#i18n{' in rest:
                        add('dom-sink', m.start(1) + s.start(), (s.group(1) + rest).strip())

for r in report:
    print(r)
print('%d finding(s)' % len(report))
