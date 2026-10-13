#!/usr/bin/env python3
"""
LUT-33153 — writes the i18n messages placed in a <script> element through the i18n() function,
JavaScript-escaped: #i18n{key} -> ${i18n('key')?js_string?no_esc}.

#i18n{key} is replaced after the rendering, as is: neither auto-escaping nor ?js_string can reach
it, and a French apostrophe ends the JavaScript string it sits in. i18n() is evaluated by the
template, with the same locale. <#noparse> blocks are left alone.

Usage: i18n-in-scripts.py <templates-dir>... [--apply]
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
I18N = re.compile(r'#i18n\{([\w.]+)\}')

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
                found = []

                def rewrite(body):
                    new, n = I18N.subn(lambda i: "${i18n('%s')?js_string?no_esc}" % i.group(1), body)
                    found.append(n)
                    return new

                body = without_ftl_tags(m.group(2), rewrite)
                count += found[0]
                if found[0]:
                    print('  %s : %d' % (path, found[0]))
                return m.group(1) + body + m.group(3)

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
print('%d message(s)' % count)
