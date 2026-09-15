#!/usr/bin/env python3
"""
Reperage des constructions FreeMarker dans une source de template (LUT-33153).

Une interpolation situee DANS une balise <#...> ou <@...> est un argument : on ne peut pas y
inserer de directive de bloc. Partage par les codemods 02, 03 et 05.
"""

def freemarker_tag_spans(src):
    """Retourne la liste des (debut, fin) des balises <#...> et <@...>."""
    spans = []
    i = 0
    n = len(src)
    while i < n - 1:
        if src[i] == "<" and src[i + 1] in "#@":
            j = i + 2
            quote = None
            depth = 0
            while j < n:
                c = src[j]
                if quote:
                    if c == quote:
                        quote = None
                elif c in "'\"":
                    quote = c
                elif c == "{":
                    depth += 1
                elif c == "}":
                    depth -= 1
                elif c == ">" and depth <= 0:
                    break
                j += 1
            spans.append((i, min(j, n - 1)))
            i = j + 1
        else:
            i += 1
    return spans


def in_tag(pos, spans):
    for a, b in spans:
        if a <= pos <= b:
            return True
        if a > pos:
            break
    return False
