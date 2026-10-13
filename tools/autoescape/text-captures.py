#!/usr/bin/env python3
"""
LUT-33153 — turns the block captures that compute a plain string into expression assignments.

With HTML auto-escaping, <#assign x>...</#assign> produces a markup value, not a string. That is
what a capture holding HTML needs, but a capture computing a flag, a code or a path is then
refused wherever a string is expected (#include, ?boolean, ?trim, comparisons). This script
rewrites those captures, one line each, when their body is one of:

    ${expr}                                  ->  x = ( expr )?string
    literal text (no tag, no entity)         ->  x = 'text'
    <#if c>A<#else>B</#if>, A and B literal text or ${expr}   ->  x = ( c )?then( A, B )
    <#if c>A</#if>                           ->  x = ( c )?then( A, '' )

A capture holding a tag or an HTML entity is left alone: it is markup. Anything else is reported.

Usage: text-captures.py <templates-dir>... [--apply]
"""
import os
import re
import sys

APPLY = '--apply' in sys.argv
ROOTS = [a for a in sys.argv[1:] if not a.startswith('--')]

CAPTURE = re.compile(r'<#(assign|local|global)\s+(\w+)\s*>(.*?)</#\1>', re.S)
INTERP = re.compile(r'^\$\{(.*)\}$', re.S)
IF_ELSE = re.compile(r'^<#if\s+(.*?)>(.*?)<#else>(.*?)</#if>$', re.S)
IF_ONLY = re.compile(r'^<#if\s+(.*?)>(.*?)</#if>$', re.S)


def literal(text):
    return "'" + text.replace('\\', '\\\\').replace("'", "\\'") + "'"


def is_plain(text):
    return not re.search(r'[<>$#&]', text) and '\n' not in text


def value(part):
    """an expression for one branch, or None"""
    m = INTERP.match(part)
    if m and '}' not in m.group(1).replace('{', ''):
        expr = m.group(1).strip()
        # ?string formats a number or a date exactly as ${...} did: the capture produced a string
        return '( %s )?string' % ( expr + "''" if expr.endswith('!') else expr )
    if is_plain(part):
        return literal(part)
    return None


def convert(body):
    if '\n' in body:
        return None
    if re.search(r'</?[a-zA-Z][^>]*>|&\w+;|&#\d+;', re.sub(r'</?#[^>]*>', '', body)):
        return None  # markup: it must stay a capture
    v = value(body)
    if v is not None:
        return v
    for pattern, has_else in ((IF_ELSE, True), (IF_ONLY, False)):
        m = pattern.match(body)
        if m and '<#' not in m.group(2) and (not has_else or '<#' not in m.group(3)):
            a = value(m.group(2))
            b = value(m.group(3)) if has_else else "''"
            if a is not None and b is not None:
                return '( %s )?then( %s, %s )' % (m.group(1).strip(), a, b)
    return None


changed, manual = 0, []
for root_dir in ROOTS:
    for root, _, files in os.walk(root_dir):
        for f in sorted(files):
            if not f.endswith(('.ftl', '.html')):
                continue
            path = os.path.join(root, f)
            src = open(path, encoding='utf-8').read()

            def repl(m):
                global changed
                expr = convert(m.group(3))
                if expr is None:
                    if not re.search(r'</?[a-zA-Z][^>]*>|&\w+;', re.sub(r'</?#[^>]*>|\$\{[^}]*\}', '', m.group(3))):
                        manual.append('%s:%d %s' % (path, src[:m.start()].count('\n') + 1, m.group(0)[:160].replace('\n', ' ')))
                    return m.group(0)
                changed += 1
                return '<#%s %s = %s />' % (m.group(1), m.group(2), expr)

            new = CAPTURE.sub(repl, src)
            if new != src and APPLY:
                open(path, 'w', encoding='utf-8').write(new)

print('%d capture(s) turned into expressions' % changed)
print('%d text capture(s) left for review:' % len(manual))
for line in manual:
    print('  ' + line)
