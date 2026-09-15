#!/usr/bin/env python3
"""
Compares the page snapshots taken with service.freemarker.templateAutoEscape=false and =true
(LUT-33153).

What we want to prove is that the two modes produce the SAME PAGE. They do not produce the same
bytes, and they are not supposed to: with auto-escaping on, an apostrophe in a label is written
`&#39;` instead of `'`. That is the feature working, and the browser renders both identically.

So the comparison decodes HTML entities on both sides before diffing: it asserts that the markup
structure and the rendered text are the same. Whether the escaping itself is correct is asserted
separately, on the raw response, by autoescape-e2e.js — a leaked tag, an attribute that lost its
delimiting quotes, or a double escaping all fail there.

Remaining volatile values (timestamps, session ids, tokens) are neutralised here.

Usage: compare-modes.py <snapshots-dir>
"""
import difflib
import html
import os
import re
import sys

VOLATILE = [
    # 15 sept. 2026, 13:25:49   /   15 sept. 2026
    (re.compile(r"\d{1,2}\s+[a-zéû]+\.?\s+\d{4}(,\s*\d{1,2}:\d{2}(:\d{2})?)?", re.I), "DATE"),
    (re.compile(r"\d{2}/\d{2}/\d{4}([ ,]*\d{1,2}:\d{2}(:\d{2})?)?"), "DATE"),
    (re.compile(r"\d{4}-\d{2}-\d{2}[T ]\d{2}:\d{2}(:\d{2})?"), "DATE"),
    (re.compile(r"\d{1,2}:\d{2}:\d{2}"), "TIME"),
    (re.compile(r"[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", re.I), "UUID"),
    (re.compile(r"\b\d{10,}\b"), "NUM"),
]


def canonical(path):
    with open(path, encoding="utf-8") as f:
        text = f.read()

    # equivalent escapings collapse to the same text: with auto-escaping on, an apostrophe is
    # written &#39; and a flag emoji &#127467;&#127479; — same page once the browser decodes them
    text = html.unescape(text)

    for pattern, replacement in VOLATILE:
        text = pattern.sub(replacement, text)

    # re-tokenise from scratch rather than trusting the stored line breaks: an HTML comment or a
    # stray newline can make the two sides split differently and produce a diff that is pure noise
    text = re.sub(r"<!--.*?-->", "", text, flags=re.S)
    text = re.sub(r"\s+", " ", text)
    text = re.sub(r">\s*<", ">\n<", text)

    return [line.strip() for line in text.split("\n") if line.strip()]


def main():
    root = sys.argv[1] if len(sys.argv) > 1 else os.path.join(os.path.dirname(os.path.abspath(__file__)), "snapshots")
    left, right = os.path.join(root, "false"), os.path.join(root, "true")

    if not os.path.isdir(left) or not os.path.isdir(right):
        print("  missing snapshot directory — both modes must have run")
        return 1

    names = sorted(set(os.listdir(left)) | set(os.listdir(right)))
    failed = 0

    for name in names:
        a, b = os.path.join(left, name), os.path.join(right, name)

        if not os.path.exists(a) or not os.path.exists(b):
            print("  %-46s MISSING on one side" % name)
            failed += 1
            continue

        la, lb = canonical(a), canonical(b)

        if la == lb:
            print("  %-46s identical" % name)
            continue

        diff = [l for l in difflib.unified_diff(la, lb, lineterm="", n=0)
                if l[:1] in "+-" and l[:3] not in ("+++", "---")]
        print("  %-46s %d differing line(s)" % (name, len(diff)))
        for line in diff[:12]:
            print("       " + line[:200])
        if len(diff) > 12:
            print("       ... %d more" % (len(diff) - 12))
        failed += 1

    print()
    if failed:
        print("  %d page(s) differ between the two modes" % failed)
        return 1

    print("  every page renders identically with auto-escaping on and off")
    return 0


if __name__ == "__main__":
    sys.exit(main())
