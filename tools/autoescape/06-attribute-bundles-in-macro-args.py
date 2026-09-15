#!/usr/bin/env python3
"""
Codemod 06 (LUT-33153) : lots d'attributs ecrits en litteral dans un appel de macro.

    <@cIcon name='x' params='aria-hidden="true"' />

En mode autoescape, la macro imprime ${params} et les guillemets DELIMITEURS de l'attribut
sont echappes : aria-hidden=&quot;true&quot;. L'attribut est casse.

La forme correcte est la capture, qui porte le markup tel quel dans les deux modes tout en
echappant les valeurs interpolees :

    <#assign paramsAttr1>aria-hidden="true"</#assign>
    <@cIcon name='x' params=paramsAttr1 />

C'est le cas C4 du ticket, et le plus repandu : 189 occurrences dans lutece-core.

Usage: 06-attribute-bundles-in-macro-args.py <racine> [--apply]
"""
import os
import re
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from fmparse import freemarker_tag_spans, in_tag  # noqa: E402

# argument de macro dont la valeur litterale contient un attribut HTML  nom="valeur"
# la valeur litterale peut contenir des quotes echappees (\'), il faut les traverser
ARG = re.compile(r"([A-Za-z_][A-Za-z0-9_]*)\s*=\s*'((?:[^'\\]|\\.)*=\"(?:[^'\\]|\\.)*)'")
INDENT = re.compile(r"^[ \t]*")


def process(src):
    lines = src.split("\n")
    out = []
    counter = 0
    depth = 0
    changed = 0

    for line in lines:
        hits = []
        if "=\"" in line:
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
                var = "%sAttr%d" % (m.group(1), counter)
                # la valeur quitte un litteral de chaine pour du texte de template :
                # les quotes echappees redeviennent des quotes ordinaires
                value = m.group(2).replace("\\'", "'").replace('\\"', '"')
                prelude.append("%s<#%s %s>%s</#%s>" % (indent, keyword, var, value, keyword))
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

    print("06. lots d'attributs hisses : %d sur %d fichiers" % (total, files))


if __name__ == "__main__":
    main()
