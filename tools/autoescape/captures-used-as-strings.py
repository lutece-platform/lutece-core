#!/usr/bin/env python3
"""
LUT-33153 — reports the block captures that a template then uses as a string.

With HTML auto-escaping a capture (<#assign x>...</#assign>) is a markup value. Printing it is fine;
comparing it (x == 'a', x != ''), or applying a string built-in to it (x?trim, x?split, x?boolean,
x?contains...) fails at render time. For each capture, the uses of its variable in the same file
are checked. Fix: an expression assignment (x = ...), see text-captures.py, or compare
x?markup_string.

Usage: captures-used-as-strings.py <templates-dir>
"""
import os
import re
import sys

CAPTURE = re.compile(r'<#(assign|local|global)\s+(\w+)\s*>')
STRING_BUILTINS = ('trim|length|split|contains|starts_with|ends_with|replace|index_of|last_index_of|matches|'
                   'lower_case|upper_case|cap_first|uncap_first|capitalize|substring|keep_after|keep_before|'
                   'keep_after_last|keep_before_last|remove_beginning|remove_ending|boolean|number|url|js_string|'
                   'json_string|word_list|left_pad|right_pad|chop_linebreaks|truncate')

found = 0
for root, _, files in os.walk(sys.argv[1]):
    for f in sorted(files):
        if not f.endswith(('.ftl', '.html')):
            continue
        path = os.path.join(root, f)
        src = re.sub(r'<#--.*?-->', lambda m: ' ' * len(m.group(0)), open(path, encoding='utf-8').read(), flags=re.S)
        names = {m.group(2) for m in CAPTURE.finditer(src)}
        for name in sorted(names):
            n = re.escape(name)
            uses = [
                r'(?<![\w.?])%s\s*(?:==|!=|=(?!=)|gt\b|lt\b|gte\b|lte\b)\s*["\'\w]' % n,
                r'["\']\s*(?:==|!=)\s*%s(?![\w?(])' % n,
                r'(?<![\w.?])%s\?(?:%s)\b' % (n, STRING_BUILTINS),
                r'<#include\s+[^>]*\$\{%s\}' % n,
            ]
            for rx in uses:
                for m in re.finditer(rx, src):
                    # inside a FreeMarker tag only: a JavaScript comparison is not FreeMarker's business
                    start = src.rfind('<', 0, m.start())
                    if start < 0 or src[start + 1] not in '#@' or '>' in src[start:m.start()].replace('->', ''):
                        if not rx.startswith('<#include'):
                            continue
                    found += 1
                    print('%s:%d  capture %s used as a string: %s' % (path, src[:m.start()].count('\n') + 1, name, m.group(0)))
print('%d use(s)' % found)
