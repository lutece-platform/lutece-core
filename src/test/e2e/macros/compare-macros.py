#!/usr/bin/env python3
"""
LUT-33387 — compares two renderings of the macro calls written by MacroHarness.

Usage: compare-macros.py <left-dir> <right-dir>

A call differs when its output differs once the insignificant differences are removed: the order
of the attributes of a tag, white space between tags and inside attribute values, and the random
numbers some macros put in generated ids. Exit status 1 when a call differs, unless its macro is
listed in accepted-diffs.txt (a change made on purpose, with its reason).
"""
import difflib
import html
import importlib.util
import os
import re
import sys

HERE = os.path.dirname(os.path.abspath(__file__))
spec = importlib.util.spec_from_file_location("compare_snapshots", os.path.join(HERE, "..", "compare-snapshots.py"))
compare_snapshots = importlib.util.module_from_spec(spec)
spec.loader.exec_module(compare_snapshots)


def calls(path):
    out = {}
    if os.path.exists(path):
        for block in open(path, encoding="utf-8").read().split("### ")[1:]:
            label, _, body = (block.split("\n", 2) + ["", ""])[:3]
            out[label] = body.strip()
    return out


def canonical(text):
    # the same character written as an entity or not renders the same: compare decoded text, so a
    # reference rendered without auto-escaping compares with the same macro rendered with it
    text = html.unescape(text)
    # a value JavaScript-escaped (?js_string) is the same value: \" and \' inside a JS string, and a
    # template literal turned into a quoted string
    text = text.replace('\\"', '"').replace("\\'", "'").replace("\\/", "/").replace('`', "'")
    text = re.sub(r"\d{8,}", "N", text)
    text = re.sub(r"\s+", " ", text)
    text = compare_snapshots.sort_attributes(text)
    text = re.sub(r">\s+", ">", text)
    text = re.sub(r"\s+<", "<", text)
    return text.strip()


def accepted():
    names = set()
    path = os.path.join(HERE, "accepted-diffs.txt")
    if os.path.exists(path):
        for line in open(path, encoding="utf-8"):
            if line.strip() and not line.startswith("#"):
                names.add(line.split()[0])
    return names


def main():
    left, right = sys.argv[1], sys.argv[2]
    allowed = accepted()
    strict = differing = expected = 0
    for name in sorted(set(os.listdir(left)) | set(os.listdir(right))):
        a, b = calls(os.path.join(left, name)), calls(os.path.join(right, name))
        for variant in sorted(set(a) | set(b)):
            x, y = a.get(variant, "<missing>"), b.get(variant, "<missing>")
            if re.sub(r"\d{8,}", "N", x) == re.sub(r"\d{8,}", "N", y):
                continue
            strict += 1
            cx, cy = canonical(x), canonical(y)
            if cx == cy:
                continue
            macro = name[:-4]
            if macro in allowed:
                expected += 1
            else:
                differing += 1
            print("=== %s [%s]%s" % (macro, variant, " (accepted)" if macro in allowed else ""))
            for line in difflib.unified_diff(cx.replace("><", ">\n<").split("\n"), cy.replace("><", ">\n<").split("\n"), lineterm="", n=1):
                if line[:3] not in ("---", "+++"):
                    print("    " + line[:300])
    print("%d call(s) differ byte-wise; once white space and attribute order are ignored: %d accepted, %d unexpected"
          % (strict, expected, differing))
    return 1 if differing else 0


if __name__ == "__main__":
    sys.exit(main())
