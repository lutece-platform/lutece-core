#!/usr/bin/env python3
"""
Codemod G (LUT-33153) : normaliser les blocs capturants qui ne portent PAS de balisage.

Regle : un bloc capturant dont le corps ne contient aucune balise HTML est une valeur de
texte (un drapeau, un libelle, un code de theme, un chemin). En mode autoescape il devient
pourtant un markup_output, et tout usage en chaine echoue :

    <#assign isRtl><#if ...>true<#else>false</#if></#assign>
    ... isRtl?boolean          ->  NonStringException

On ajoute donc une normalisation apres la capture :

    <#assign isRtl = isRtl?is_markup_output?then(isRtl?markup_string, isRtl) />

Les captures qui contiennent reellement du balisage (lots d'attributs, fragments HTML) sont
laissees intactes : c'est precisement pour elles que le markup_output est le bon type.

Usage: codemod_text_captures.py <racine> [--apply] [--report]
"""
import os
import re
import sys

CAPTURE = re.compile(r"<#(assign|local|global)\s+([A-Za-z_][A-Za-z0-9_]*)\s*>")
# une vraie balise HTML ouvrante ou fermante : <div ...>, </span>, <br/>
HTML_TAG = re.compile(r"</?[a-zA-Z][a-zA-Z0-9]*(\s|/|>)")
# un lot d'attributs sans balise :  title="x"   data-bs-toggle='y'
ATTRIBUTE = re.compile(r"[a-zA-Z-]+\s*=\s*[\"']")

NORMALIZE = "<#{kw} {p} = {p}?is_markup_output?then({p}?markup_string, {p}) />"


def carries_markup(body):
    """Le corps capture porte-t-il du HTML (balise, lot d'attributs, sortie de macro) ?"""
    # une capture qui echappe explicitement, ou qui capte la sortie d'une macro, EST du markup
    if "<#outputformat" in body or "<#noautoesc" in body or re.search(r"<@[a-zA-Z]", body):
        return True

    # neutraliser les directives FreeMarker et les interpolations, qui ne sont pas du HTML
    stripped = re.sub(r"<#[^>]*>|</#[a-z]+>", "", body)
    stripped = re.sub(r"\$\{[^}]*\}", "", stripped)
    return bool(HTML_TAG.search(stripped)) or bool(ATTRIBUTE.search(stripped))


def process(src):
    out, last, count = [], 0, 0
    report = []

    for m in CAPTURE.finditer(src):
        kw, var = m.group(1), m.group(2)
        close = "</#%s>" % kw
        end = src.find(close, m.end())
        if end == -1:
            continue

        body = src[m.end():end]
        after = end + len(close)

        if carries_markup(body):
            continue
        if ("<#%s %s = %s?is_markup_output" % (kw, var, var)) in src[after:after + 220]:
            continue

        out.append(src[last:after])
        out.append("\n" + NORMALIZE.format(kw=kw, p=var))
        last = after
        count += 1
        report.append(var)

    out.append(src[last:])
    return "".join(out), count, report


def main():
    root = sys.argv[1]
    apply_changes = "--apply" in sys.argv
    verbose = "--report" in sys.argv
    total = files = 0

    for dirpath, _, filenames in os.walk(root):
        for fn in sorted(filenames):
            if not (fn.endswith(".ftl") or fn.endswith(".html")):
                continue
            path = os.path.join(dirpath, fn)
            with open(path, encoding="utf-8") as f:
                src = f.read()
            new, n, report = process(src)
            if n:
                files += 1
                total += n
                print("  %-70s %2d  %s" % (path.split("templates/")[-1], n,
                                           ", ".join(report[:5]) + ("..." if len(report) > 5 else "") if verbose else ""))
                if apply_changes:
                    with open(path, "w", encoding="utf-8") as f:
                        f.write(new)

    print("G. captures de texte normalisees : %d sur %d fichiers" % (total, files))


if __name__ == "__main__":
    main()
