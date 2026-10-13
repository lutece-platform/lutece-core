#!/usr/bin/env python3
"""Generate macro call variants from a templates tree (the baseline).

Output: JSON lines {"macro": name, "variant": label, "ftl": "<@...>"}
The same call file is replayed against the baseline and the modified tree, so both are
rendered with exactly the same arguments.
"""
import json, os, re, sys

ROOT = sys.argv[1]


def macro_headers(src):
    i = 0
    while True:
        i = src.find('<#macro ', i)
        if i < 0:
            return
        j = i + len('<#macro ')
        depth, quote = 0, None
        k = j
        while k < len(src):
            c = src[k]
            if quote:
                if c == quote:
                    quote = None
            elif c in '\'"':
                quote = c
            elif c in '[{(':
                depth += 1
            elif c in ']})':
                depth -= 1
            elif c == '>' and depth == 0:
                break
            k += 1
        end = src.find('</#macro>', k)
        yield src[j:k].rstrip('/').strip(), src[k:end if end > 0 else len(src)]
        i = k


def split_params(header):
    """name, [(param, default-or-None, catchall)]"""
    toks, cur, depth, quote = [], '', 0, None
    for c in header + ' ':
        if quote:
            cur += c
            if c == quote:
                quote = None
            continue
        if c in '\'"':
            quote = c; cur += c; continue
        if c in '[{(':
            depth += 1
        if c in ']})':
            depth -= 1
        if c.isspace() and depth == 0:
            if cur:
                toks.append(cur)
            cur = ''
        else:
            cur += c
    # rejoin "a = b" written with spaces
    merged = []
    for t in toks:
        if merged and (t.startswith('=') or merged[-1].endswith('=')):
            merged[-1] += t
        else:
            merged.append(t)
    name, params = merged[0], []
    for t in merged[1:]:
        if t.endswith('...'):
            params.append((t[:-3], None, True))
        elif '=' in t:
            p, d = t.split('=', 1)
            params.append((p.strip(), d.strip(), False))
        else:
            params.append((t, None, False))
    return name, params


def sample(p, default):
    """A non-default value of the same kind as the default."""
    if default is None:
        return "'v_%s'" % p
    d = default
    if d in ('true', 'false'):
        return 'false' if d == 'true' else 'true'
    if re.fullmatch(r'-?\d+', d):
        return str(int(d) + 1)
    if d.startswith('['):
        return "['md']"
    if d.startswith('{'):
        return None  # hashes are too specific to fake
    if d[0] in '\'"':
        fixed = {
            'id': "'my-id'", 'class': "'my-class'", 'params': "'data-x=\"1\"'",
            'title': "'My title'", 'align': "'center'", 'size': "'sm'", 'color': "'primary'",
            'type': "'text'", 'name': "'my-name'", 'value': "'my-value'", 'href': "'https://e.x/a?b=1&c=2'",
        }
        return fixed.get(p, "'v_%s'" % p)
    return None  # expression default: leave it


macros = {}
literals = {}
order = []
for root, _, files in os.walk(ROOT):
    for f in sorted(files):
        if not f.endswith(('.ftl', '.html')):
            continue
        src = open(os.path.join(root, f), encoding='utf-8', errors='replace').read()
        for h, body in macro_headers(src):
            try:
                name, params = split_params(h)
            except Exception:
                continue
            if not re.fullmatch(r'\w+', name):
                continue
            if name not in macros:
                order.append(name)
                macros[name] = {}
                literals[name] = {}
            for p, d, ca in params:
                if not ca:
                    macros[name].setdefault(p, d)
                    # the values the body compares the parameter with: type='textarea', style?contains('modal')
                    for v in re.findall(r"(?<![\w.?])%s\s*(?:==?|!=)\s*'([^'$]+)'|(?<![\w.?])%s\?(?:contains|starts_with)\(\s*'([^'$]+)'" % (p, p), body):
                        value = v[0] or v[1]
                        literals[name].setdefault(p, [])
                        if value not in literals[name][p] and len(literals[name][p]) < 40:
                            literals[name][p].append(value)

PROBE = '--attrs-probe' in sys.argv

out = []
for name in sorted(order):
    params = macros[name]
    required = {p: sample(p, None) for p, d in params.items() if d is None}
    if PROBE:
        # one call per macro declaring attrs, with a probe attribute
        if 'attrs' in params:
            a = ' '.join('%s=%s' % (k, v) for k, v in required.items())
            out.append({'macro': name, 'variant': 'attrs-probe',
                        'ftl': '<@%s %s attrs={"data-attrs-probe": "p"}>NESTED</@%s>' % (name, a, name)})
        continue

    def call(args, label):
        a = ' '.join('%s=%s' % (k, v) for k, v in args.items() if v is not None)
        out.append({'macro': name, 'variant': label,
                    'ftl': '<@%s %s>NESTED</@%s>' % (name, a, name)})

    call(dict(required), 'required')
    full = dict(required)
    for p, d in params.items():
        if d is not None and not (d in ('true', 'false') or d.startswith('[')):
            s = sample(p, d)
            if s is not None:
                full[p] = s
    call(full, 'strings')
    # flip each boolean / list param alone, on top of the full string set
    for p, d in params.items():
        if d is not None and (d in ('true', 'false') or d.startswith('[')):
            v = dict(full)
            v[p] = sample(p, d)
            call(v, 'flip-' + p)
    # each value the body compares a string parameter with
    for p, values in sorted(literals[name].items()):
        if params.get(p) is not None and params[p][:1] not in "'\"":
            continue  # not a string parameter
        for value in values:
            if re.search(r'&(#\d+|[a-zA-Z]+);', value):
                continue  # an entity in a literal is a defect of the reference, fixed by LUT-33153
            v = dict(full)
            v[p] = "'%s'" % value.replace("'", "\\'")
            call(v, 'value-%s=%s' % (p, value))
    # special characters in the common trio
    if {'id', 'class', 'params'} & set(params):
        v = dict(full)
        for p in ('class', 'title'):
            if p in params:
                v[p] = '"a&b \'q\' <t>"'
        call(v, 'specials')

for o in out:
    print(json.dumps(o))
print('%d macros, %d calls' % (len(order), len(out)), file=sys.stderr)
