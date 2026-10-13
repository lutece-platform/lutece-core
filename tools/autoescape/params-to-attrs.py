#!/usr/bin/env python3
"""
LUT-33153 — turns a literal attribute bundle passed to a macro into an attrs hash:

    params='title="${x}" data-step="${n}" hidden'   ->   attrs={'title': x, 'data-step': n, 'hidden': true}

params is written as is by commonAttrs (it is HTML written by the template author), so a value
interpolated into it is not escaped. attrs carries data: the macro writes the structure and the
value is escaped. By default only the bundles that interpolate a value are converted (--all: every
bundle). A value that is a single interpolation becomes the expression itself; any other value stays
a string literal; a valueless attribute becomes true. A bundle that cannot be parsed, or that also
interpolates a whole bundle (${otherParams}), is reported and left alone.

Usage: params-to-attrs.py <macros-writing-attrs.txt> <path>... [--apply] [--all]
"""
import os
import re
import sys

APPLY = '--apply' in sys.argv
ALL = '--all' in sys.argv
args = [a for a in sys.argv[1:] if not a.startswith('--')]
OK_MACROS = set(open(args[0]).read().split())
PATHS = args[1:]

CALL = re.compile(r"(<@([\w.]+)\b(?:[^>'\"]|'(?:[^'\\]|\\.)*'|\"(?:[^\"\\]|\\.)*\")*?)\bparams\s*=\s*'((?:[^'\\]|\\.)*)'")
ATTR = re.compile(r"""\s*([A-Za-z_:][-\w:.]*)(?:\s*=\s*(?:"([^"]*)"|'([^'"]*)'|([^\s"'=<>`]+)))?""")


def value_expr(raw):
    raw = raw.replace("\\'", "'")
    whole = re.fullmatch(r"\$\{([^{}]*)\}", raw)
    if whole:
        return '( %s )' % whole.group(1).strip()
    if '"' in raw or '\\' in raw:
        return None
    return '"%s"' % raw


def to_attrs(bundle):
    pos, pairs = 0, []
    while pos < len(bundle):
        if not bundle[pos:].strip():
            break
        m = ATTR.match(bundle, pos)
        if not m or m.end() == pos:
            return None
        raw = next((g for g in m.groups()[1:] if g is not None), None)
        if raw is None:
            pairs.append("'%s': true" % m.group(1))
        else:
            expr = value_expr(raw)
            if expr is None:
                return None
            pairs.append("'%s': %s" % (m.group(1), expr))
        pos = m.end()
    names = [p.split("'")[1] for p in pairs]
    if not pairs or len(set(names)) != len(names):
        return None
    return '{' + ', '.join(pairs) + '}'


converted, skipped = 0, []
for base in PATHS:
    walk = os.walk(base) if os.path.isdir(base) else [(os.path.dirname(base), None, [os.path.basename(base)])]
    for root, _, files in walk:
        for f in sorted(files):
            if not f.endswith(('.ftl', '.html')):
                continue
            path = os.path.join(root, f)
            src = open(path, encoding='utf-8').read()

            def repl(m):
                global converted
                macro, bundle = m.group(2), m.group(3)
                if not bundle.strip() or (not ALL and '${' not in bundle):
                    return m.group(0)
                line = src[:m.start()].count('\n') + 1
                if macro not in OK_MACROS or re.search(r"\battrs\s*=", m.group(1)):
                    skipped.append('%s:%d @%s (macro without attrs) %s' % (path, line, macro, bundle))
                    return m.group(0)
                h = to_attrs(bundle)
                if h is None:
                    skipped.append('%s:%d @%s (bundle not parsed) %s' % (path, line, macro, bundle))
                    return m.group(0)
                converted += 1
                return '%sattrs=%s' % (m.group(1), h)

            new = CALL.sub(repl, src)
            if new != src and APPLY:
                open(path, 'w', encoding='utf-8').write(new)

print('converted %d' % converted)
print('skipped %d:' % len(skipped))
for x in skipped:
    print('  ' + x)
