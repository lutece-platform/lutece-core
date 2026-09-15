#!/usr/bin/env python3
"""
Codemods rendant les templates FreeMarker de lutece-core tolerants aux deux modes
d'autoescape (LUT-33153).

A. comparaisons      X != ''  ->  X?has_content      X == ''  ->  !X?has_content
B. normalisation     parametres de macro subissant une operation de chaine
C. echappement legacy  ?html / ?xhtml  ->  <#outputformat "HTML">...</#outputformat>
                       (uniquement hors d'une balise FreeMarker ; le reste est signale)

Usage: codemod.py <racine> [--apply] [--only=cmp,norm,esc]
"""
import re
import sys
import os

from fmparse import freemarker_tag_spans, in_tag


# =========================================================================== #
# A. comparaisons a la chaine vide
# =========================================================================== #

# identifiant, chemin pointe, appel de builtin : class / paginator.labelFirst / class?trim
EXPR = r"[A-Za-z_][A-Za-z0-9_]*(?:\.[A-Za-z_][A-Za-z0-9_]*)*(?:\?[a-z_]+)*"
CMP_NE = re.compile(r"(?<![\w?.!])(" + EXPR + r")\s*!=\s*(?:''|\"\")")
CMP_EQ = re.compile(r"(?<![\w?.!<>])(" + EXPR + r")\s*==\s*(?:''|\"\")")


def codemod_comparisons(src):
    n = [0]

    def ne(m):
        n[0] += 1
        return "%s?has_content" % m.group(1)

    def eq(m):
        n[0] += 1
        return "!%s?has_content" % m.group(1)

    src = CMP_NE.sub(ne, src)
    src = CMP_EQ.sub(eq, src)
    return src, n[0]


# =========================================================================== #
# B. normalisation des parametres de macro
# =========================================================================== #

MACRO_RE = re.compile(r"<#macro\s+([A-Za-z_][A-Za-z0-9_]*)([^>]*)>", re.S)
PARAM_RE = re.compile(r"([A-Za-z_][A-Za-z0-9_]*)\s*=\s*(?:''|\"\")")

STRING_BUILTINS = (
    "trim", "length", "contains", "replace", "split", "substring", "upper_case",
    "lower_case", "starts_with", "ends_with", "index_of", "last_index_of",
    "ensure_ends_with", "ensure_starts_with", "cap_first", "uncap_first",
    "capitalize", "chop_linebreak", "js_string", "json_string", "url",
    "keep_before", "keep_after", "matches", "word_list", "number",
)

NORMALIZE = "<#local {p} = {p}?is_markup_output?then({p}?markup_string, {p}) />"


def macro_body(src, decl_end):
    end = src.find("</#macro>", decl_end)
    return src[decl_end:end if end != -1 else len(src)]


def needs_normalization(body, param):
    p = re.escape(param)
    if re.search(r"<#local\s+%s\s*\+=" % p, body):
        return True
    if re.search(r"<#local\s+%s\s*=\s*%s\s*\+" % (p, p), body):
        return True
    for bi in STRING_BUILTINS:
        if re.search(r"(?<![\w.])%s\?%s(?![\w])" % (p, bi), body):
            return True
    return False


def codemod_normalize(src):
    out, last, count = [], 0, 0

    for m in MACRO_RE.finditer(src):
        decl_end = m.end()
        params = PARAM_RE.findall(m.group(2))
        if not params:
            continue

        body = macro_body(src, decl_end)
        needed = [
            p for p in params
            if needs_normalization(body, p)
            and ("<#local %s = %s?is_markup_output" % (p, p)) not in body
        ]
        if not needed:
            continue

        out.append(src[last:decl_end])
        out.append("\n" + "\n".join(NORMALIZE.format(p=p) for p in needed))
        last = decl_end
        count += len(needed)

    out.append(src[last:])
    return "".join(out), count


# =========================================================================== #
# C. ?html / ?xhtml
# =========================================================================== #

LEGACY = re.compile(r"\$\{([^{}]+?)\?x?html\}")


def codemod_legacy_escape(src):
    spans = freemarker_tag_spans(src)
    inside = []
    count = [0]

    def repl(m):
        if in_tag(m.start(), spans):
            inside.append(m.group(0))
            return m.group(0)
        count[0] += 1
        return '<#outputformat "HTML">${%s}</#outputformat>' % m.group(1)

    return LEGACY.sub(repl, src), count[0], inside


# =========================================================================== #

def main():
    root = sys.argv[1]
    apply_changes = "--apply" in sys.argv
    only = None
    for a in sys.argv[2:]:
        if a.startswith("--only="):
            only = a.split("=", 1)[1].split(",")

    t = {"cmp": 0, "norm": 0, "esc": 0, "files": 0}
    manual = []

    for dirpath, _, filenames in os.walk(root):
        for fn in sorted(filenames):
            if not (fn.endswith(".ftl") or fn.endswith(".html")):
                continue
            path = os.path.join(dirpath, fn)
            with open(path, encoding="utf-8") as f:
                src = original = f.read()

            c = n = e = 0
            if only is None or "esc" in only:
                src, e, ins = codemod_legacy_escape(src)
                manual += [(path, x) for x in ins]
            if only is None or "cmp" in only:
                src, c = codemod_comparisons(src)
            if only is None or "norm" in only:
                src, n = codemod_normalize(src)

            if src != original:
                t["files"] += 1
                t["cmp"] += c
                t["norm"] += n
                t["esc"] += e
                if apply_changes:
                    with open(path, "w", encoding="utf-8") as f:
                        f.write(src)

    print("fichiers modifies        : %d" % t["files"])
    print("A. comparaisons          : %d" % t["cmp"])
    print("B. normalisations        : %d" % t["norm"])
    print("C. ?html/?xhtml convertis: %d" % t["esc"])
    print("C. restants DANS une balise (traitement manuel) : %d sur %d fichiers"
          % (len(manual), len(set(p for p, _ in manual))))
    for p in sorted(set(p for p, _ in manual)):
        k = sum(1 for q, _ in manual if q == p)
        print("   %-78s %d" % (p.split("templates/")[-1], k))


if __name__ == "__main__":
    main()
