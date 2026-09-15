#!/usr/bin/env python3
"""
Codemod D (LUT-33153) : ?html / ?xhtml situes DANS une balise FreeMarker.

Une directive de bloc ne peut pas etre inseree dans un argument de macro. On hisse
donc la valeur echappee dans une capture, juste avant la balise :

    <@input value='${x?html}' />
devient
    <#local escValue1><#outputformat "HTML">${x}</#outputformat></#local>
    <@input value='${escValue1}' />

La capture produit un markup_output en mode true et une String en mode false ; dans
les deux cas la macro l'imprime telle quelle. Rendu identique, echappement conserve.

Usage: codemod_hoist.py <racine> [--apply]
"""
import re
import sys
import os

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from fmparse import freemarker_tag_spans, in_tag  # noqa: E402

LEGACY = re.compile(r"\$\{([^{}]+?)\?x?html\}")
INDENT = re.compile(r"^[ \t]*")


def process(src):
    lines = src.split("\n")
    out = []
    counter = 0
    depth = 0  # profondeur <#macro>
    changed = 0

    for line in lines:
        hits = []
        if "?html" in line or "?xhtml" in line:
            spans = freemarker_tag_spans(line)
            for m in LEGACY.finditer(line):
                if in_tag(m.start(), spans):
                    hits.append(m)

        if hits:
            indent = INDENT.match(line).group(0)
            keyword = "local" if depth > 0 else "assign"
            prelude = []
            new_line = line
            # de droite a gauche pour garder les offsets valides
            for m in reversed(hits):
                counter += 1
                var = "esc%d" % counter
                prelude.append(
                    '%s<#%s %s><#outputformat "HTML">${%s}</#outputformat></#%s>'
                    % (indent, keyword, var, m.group(1), keyword)
                )
                new_line = new_line[:m.start()] + "${%s}" % var + new_line[m.end():]
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
    total, files = 0, 0

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
                print("  %-78s %d" % (path.split("templates/")[-1], n))
                if apply_changes:
                    with open(path, "w", encoding="utf-8") as f:
                        f.write(new)

    print("D. hissages : %d sur %d fichiers" % (total, files))


if __name__ == "__main__":
    main()
