#!/usr/bin/env python3
"""
Codemod H (LUT-33153) : entites HTML ecrites a la main dans un argument de macro.

    <@aButton href='...?page_id=${id}&amp;param_block=2' />

Le '&amp;' du source est un echappement manuel, l'equivalent d'un ?html. En mode
autoescape, la chaine passe par le ${href} de la macro et le '&' est echappe une seconde
fois : '&amp;amp;'. On hisse donc la valeur dans une capture, qui porte le markup tel quel
dans les deux modes :

    <#assign hrefEnt1>...?page_id=${id}&amp;param_block=2</#assign>
    <@aButton href=hrefEnt1 />

Usage: codemod_entities.py <racine> [--apply]
"""
import os
import re
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from fmparse import freemarker_tag_spans, in_tag  # noqa: E402

# argument de macro dont la valeur litterale contient une entite HTML
ARG = re.compile(r"([A-Za-z_][A-Za-z0-9_]*)\s*=\s*'([^']*&(?:amp|lt|gt|quot|apos|nbsp|#\d+);[^']*)'")
INDENT = re.compile(r"^[ \t]*")


def process(src):
    lines = src.split("\n")
    out = []
    counter = 0
    depth = 0
    changed = 0

    for line in lines:
        hits = []
        if "&amp;" in line or "&lt;" in line or "&gt;" in line or "&quot;" in line or "&nbsp;" in line:
            spans = freemarker_tag_spans(line)
            for m in ARG.finditer(line):
                if in_tag(m.start(), spans):
                    hits.append(m)

        if hits:
            indent = INDENT.match(line).group(0)
            keyword = "local" if depth > 0 else "assign"
            prelude = []
            new_line = line
            for m in reversed(hits):
                counter += 1
                var = "%sEnt%d" % (m.group(1), counter)
                prelude.append("%s<#%s %s>%s</#%s>" % (indent, keyword, var, m.group(2), keyword))
                new_line = new_line[:m.start()] + "%s=%s" % (m.group(1), var) + new_line[m.end():]
            out.extend(reversed(prelude))
            out.append(new_line)
            changed += len(hits)
        else:
            out.append(line)

        depth += len(re.findall(r"<#macro\b", line))
        depth -= len(re.findall(r"</#macro>", line))
        depth = max(depth, 0)

    return "\n".join(out), changed


def main():
    root = sys.argv[1]
    apply_changes = "--apply" in sys.argv
    total = files = 0

    for dirpath, _, filenames in os.walk(root):
        for fn in sorted(filenames):
            if not (fn.endswith(".ftl") or fn.endswith(".html")):
                continue
            path = os.path.join(dirpath, fn)
            with open(path, encoding="utf-8") as f:
                src = f.read()
            new, n = process(src)
            if n:
                files += 1
                total += n
                print("  %-70s %d" % (path.split("templates/")[-1], n))
                if apply_changes:
                    with open(path, "w", encoding="utf-8") as f:
                        f.write(new)

    print("H. entites hissees : %d sur %d fichiers" % (total, files))


if __name__ == "__main__":
    main()
