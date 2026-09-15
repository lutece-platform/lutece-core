#!/usr/bin/env python3
"""
Codemod E (LUT-33153) : l'egalite FreeMarker a un seul '=' comparee a la chaine vide.

    <#if id=''>        ->  <#if !id?has_content>
    <#elseif text = ''>->  <#elseif !text?has_content>

Meme motif que les comparisons '!=' deja traitees : elles levent
"Can't compare values of these types" des qu'un appelant passe un bloc capture.

La transformation est strictement bornee aux balises <#if> et <#elseif> pour ne jamais
toucher a une affectation JavaScript, qui s'ecrit exactement pareil.

Usage: codemod_single_eq.py <racine> [--apply]
"""
import os
import re
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from fmparse import freemarker_tag_spans  # noqa: E402

TAG = re.compile(r"^<#(if|elseif)\b", re.I)
# identifiant / chemin / builtin, precede ni de '!' '<' '>' '=' (donc pas !=, <=, >=, ==)
EXPR = r"[A-Za-z_][A-Za-z0-9_]*(?:\.[A-Za-z_][A-Za-z0-9_]*)*(?:\?[a-z_]+)*"
CMP = re.compile(r"(?<![\w?.!<>=])(" + EXPR + r")\s*=(?!=)\s*(?:''|\"\")")


def process(src):
    spans = freemarker_tag_spans(src)
    out = []
    last = 0
    count = 0

    for a, b in spans:
        tag = src[a:b + 1]
        if not TAG.match(tag):
            continue
        new_tag, n = CMP.subn(lambda m: "!%s?has_content" % m.group(1), tag)
        if n:
            out.append(src[last:a])
            out.append(new_tag)
            last = b + 1
            count += n

    out.append(src[last:])
    return "".join(out), count


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
                print("  %-74s %d" % (path.split("templates/")[-1], n))
                if apply_changes:
                    with open(path, "w", encoding="utf-8") as f:
                        f.write(new)

    print("E. egalites simples converties : %d sur %d fichiers" % (total, files))


if __name__ == "__main__":
    main()
