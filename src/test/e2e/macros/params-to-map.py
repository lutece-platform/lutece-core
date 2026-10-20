#!/usr/bin/env python3
"""LUT-33387: rewrites the calls of a call file for templates where params is a map. The calls are generated from the
reference revision, whose params is a string of attributes (params='data-x="1" hidden'): each such literal, and the one of an argument named
like params (titleParams, additionalParameters...), becomes the equivalent map (params={'data-x': '1', 'hidden': true}), so both sides receive the same attributes.
Usage: params-to-map.py <calls.jsonl> > <calls-map.jsonl>"""
import json
import re
import sys

LITERAL = re.compile(r"""\b(\w*[Pp]arams\w*|additionalParameters)='((?:[^'\\]|\\.)*)'""")
SAMPLE = re.compile(r"v_\w+")
ATTRIBUTE = re.compile(r"""([^\s=]+)(?:="([^"]*)")?""")


def to_map(match):
    """Converts one params string literal to a map literal; a bare sample value (v_name) is no attribute."""
    argument, text = match.group(1), match.group(2).replace('\\"', '"')
    if SAMPLE.fullmatch(text) or not text.strip():
        return "%s={}" % argument
    entries = []
    for name, value in ATTRIBUTE.findall(text):
        if value == '' and ('%s=""' % name) not in text:
            entries.append("'%s': true" % name)
        else:
            entries.append("'%s': '%s'" % (name, value.replace("'", "\\'")))
    return "%s={%s}" % (argument, ", ".join(entries))


for line in sys.stdin if len(sys.argv) < 2 else open(sys.argv[1], encoding='utf-8'):
    call = json.loads(line)
    call['ftl'] = LITERAL.sub(to_map, call['ftl'])
    print(json.dumps(call, ensure_ascii=False))
