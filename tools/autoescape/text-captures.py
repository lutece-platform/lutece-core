#!/usr/bin/env python3
"""
LUT-33153 — turns the block captures that compute a plain string into expression assignments.

With HTML auto-escaping, <#assign x>...</#assign> produces a markup value, not a string. That is
what a capture holding HTML needs, but a capture computing a flag, a code or a path is then
refused wherever a string is expected (#include, ?boolean, ?trim, comparisons). This script
rewrites those captures, one line each, when their body only computes text:

    literal text (#i18n{...} included)       ->  'text'
    ${expr}                                  ->  ( expr )?string
    <#if c>A<#elseif d>B<#else>C</#if>       ->  ( c )?then( A, ( d )?then( B, C ) )
    <#list xs as x>A</#list>                 ->  ( xs )?map( x -> A )?join( '' )

and a sequence of those is joined with +. ?then and the lambda of ?map are evaluated lazily, as the
directives were. A capture holding a tag, an HTML entity or attributes (name="..."), or passed as params (directly or
through a copy), is left alone: it is markup, and the values it interpolates are escaped. A list using
its loop variables (?index, ?has_next...) or <#sep> is left alone and reported.

Usage: text-captures.py <templates-dir>... [--apply]
"""
import os
import re
import sys

APPLY = '--apply' in sys.argv
ROOTS = [a for a in sys.argv[1:] if not a.startswith('--')]

CAPTURE = re.compile(r'<#(assign|local|global)\s+(\w+)\s*>(.*?)</#\1>', re.S)


def literal(text):
    return "'" + text.replace('\\', '\\\\').replace("'", "\\'").replace('\r', '\\r').replace('\n', '\\n') + "'"


TAG = re.compile(r'<(/?)#(\w+)\b((?:[^>"\']|"[^"]*"|\'[^\']*\')*)>')


def interp_end(body, i):
    """end of the ${...} starting at i, braces balanced, or -1"""
    depth = 0
    for j in range(i + 1, len(body)):
        if body[j] == '{':
            depth += 1
        elif body[j] == '}':
            depth -= 1
            if depth == 0:
                return j + 1
    return -1


def block(body, i, name):
    """for the <#name ...> at i: (end of its closing tag, [(header, content), ...]) split on <#else>/<#elseif>
    at its own depth, or None"""
    depth, parts, head = 0, [], TAG.match(body, i)
    pos, cur_head = head.end(), head.group(3).strip()
    for m in TAG.finditer(body, head.end()):
        closing, tag = m.group(1) == '/', m.group(2)
        if tag in ('if', 'list') and not closing:
            depth += 1
        elif tag in ('if', 'list') and closing:
            if depth == 0:
                if tag != name:
                    return None
                parts.append((cur_head, body[pos:m.start()]))
                return m.end(), parts
            depth -= 1
        elif depth == 0 and name == 'if' and tag in ('else', 'elseif'):
            parts.append((cur_head, body[pos:m.start()]))
            cur_head, pos = ('else' if tag == 'else' else m.group(3).strip()), m.end()
        elif depth == 0 and tag in ('sep', 'items', 'break', 'continue', 'return', 'assign', 'local', 'global', 'include', 'import', 'nested'):
            return None
    return None


def expr_of(body):
    """an expression computing the text of a capture body, or None when the body holds markup"""
    out, i = [], 0
    while i < len(body):
        if body.startswith('${', i):
            j = interp_end(body, i)
            if j < 0:
                return None
            e = body[i + 2:j - 1].strip()
            out.append('( %s )?string' % (e + "''" if e.endswith('!') else e))
            i = j
        elif body.startswith('<#if', i) or body.startswith('<#list', i):
            name = 'if' if body.startswith('<#if', i) else 'list'
            b = block(body, i, name)
            if b is None:
                return None
            j, parts = b
            if name == 'if':
                expr, tail = "''", parts[::-1]
                if tail[0][0] == 'else':
                    e = expr_of(tail[0][1])
                    if e is None:
                        return None
                    expr, tail = e, tail[1:]
                for cond, content in tail:
                    e = expr_of(content)
                    if e is None:
                        return None
                    expr = '( %s )?then( %s, %s )' % (cond, e, expr)
                out.append(expr)
            else:
                m = re.fullmatch(r'(.+?)\s+as\s+(\w+)', parts[0][0], re.S)
                if not m or len(parts) != 1 or re.search(r'\b%s\?(index|counter|has_next|is_last|is_first|item_)' % m.group(2), parts[0][1]):
                    return None
                e = expr_of(parts[0][1])
                if e is None:
                    return None
                out.append("( %s )?map( %s -> %s )?join( '' )" % (m.group(1).strip(), m.group(2), e))
            i = j
        elif body[i] == '<' or re.match(r'&(\w+|#\d+);', body[i:]):
            return None
        else:
            j = i + 1
            while j < len(body) and not body.startswith('${', j) and body[j] != '<' and not re.match(r'&(\w+|#\d+);', body[j:]):
                j += 1
            if re.search(r'#\{', body[i:j]):
                return None
            out.append(literal(body[i:j]))
            i = j
    if not out:
        return "''"
    return out[0] if len(out) == 1 else ' + '.join(out)


def convert(body):
    if re.search(r'</?[a-zA-Z][^>]*>|&\w+;|&#\d+;', re.sub(r'</?#[^>]*>', '', body)):
        return None  # markup: it must stay a capture
    if re.search(r'[\w-]\s*=\s*["\']', re.sub(r'<#[^>]*>|\$\{[^}]*\}', '', body)):
        return None  # attributes (params): as a capture the values it interpolates are escaped, as a string they would not be
    return expr_of(body)


changed, manual = 0, []
for root_dir in ROOTS:
    for root, _, files in os.walk(root_dir):
        for f in sorted(files):
            if not f.endswith(('.ftl', '.html')):
                continue
            path = os.path.join(root, f)
            src = open(path, encoding='utf-8', newline='').read()

            def repl(m):
                global changed
                expr = convert(m.group(3))
                if expr is not None and re.search(r'\bparams\s*=\s*\(?\s*%s\b|<#(assign|local|global)\s+\w+\s*=\s*%s\s*/?>' % ((re.escape(m.group(2)),) * 2), src):
                    expr = None  # written as is through params, possibly after a copy: as a capture its values stay escaped
                if expr is None:
                    if not re.search(r'</?[a-zA-Z][^>]*>|&\w+;', re.sub(r'</?#[^>]*>|\$\{[^}]*\}', '', m.group(3))):
                        manual.append('%s:%d %s' % (path, src[:m.start()].count('\n') + 1, m.group(0)[:160].replace('\n', ' ')))
                    return m.group(0)
                changed += 1
                return '<#%s %s = %s />' % (m.group(1), m.group(2), expr)

            new = CAPTURE.sub(repl, src)
            if new != src and APPLY:
                open(path, 'w', encoding='utf-8', newline='').write(new)

print('%d capture(s) turned into expressions' % changed)
print('%d text capture(s) left for review:' % len(manual))
for line in manual:
    print('  ' + line)
