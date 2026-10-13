#!/usr/bin/env python3
"""
Compares two sets of page snapshots written by pages-e2e.js (LUT-33387), typically develop
("before") against the refactored macros ("after"). Originally written for LUT-33153 to compare
the two values of service.freemarker.templateAutoEscape.

What we want to prove is that the two runs produce the SAME PAGE, not the same bytes: an
apostrophe written `&#39;` instead of `'`, or a class list written `"a  b "` instead of `"a b"`,
renders identically in the browser.

So the comparison decodes HTML entities on both sides and collapses the white space inside
attribute values before diffing: it asserts that the markup structure and the rendered text are
the same. Whether the page itself is sound (no error page, no leaked markup, no mangled attribute)
is asserted separately, on the raw response, by pages-e2e.js.

Remaining volatile values (timestamps, session ids, tokens) are neutralised here.

Usage: compare-snapshots.py <left-dir> <right-dir>
"""
import difflib
import html
import os
import re
import sys

TAG = re.compile(r"<([a-zA-Z][\w:-]*)((?:\s+[^\s=<>\"'/]+(?:\s*=\s*(?:\"[^\"]*\"|'[^']*'|[^\s\"'<>]+))?)*)\s*(/?)>")
ATTR = re.compile(r"([^\s=<>\"'/]+)(?:\s*=\s*(\"[^\"]*\"|'[^']*'|[^\s\"'<>]+))?")


def sort_attributes(text):
    """The order of the attributes of a tag is not significant: write them sorted, double-quoted,
    with the white space of class lists collapsed."""
    def tag(m):
        attrs = []
        for a in ATTR.finditer(m.group(2)):
            value = a.group(2)
            if value is None:
                attrs.append(a.group(1))
                continue
            if value[:1] in "\"'":
                value = value[1:-1]
            value = re.sub(r"\s+", " ", value).strip()
            if a.group(1) == "class" and not value:
                continue  # class="" is no class at all
            attrs.append('%s="%s"' % (a.group(1), value))
        return "<%s%s%s>" % (m.group(1), "".join(" " + a for a in sorted(attrs)), m.group(3))
    # an attribute glued to the closing quote of the previous one (type="button"title="x") is
    # accepted by browsers: separate them first
    text = re.sub(r"(=\"[^\"<>]*\"|='[^'<>]*')(?=[A-Za-z_:][-\w:.]*=)", r"\1 ", text)
    return TAG.sub(tag, text)

VOLATILE = [
    # 15 sept. 2026, 13:25:49   /   15 sept. 2026
    (re.compile(r"\d{1,2}\s+[a-zéû]+\.?\s+\d{4}(,\s*\d{1,2}:\d{2}(:\d{2})?)?", re.I), "DATE"),
    (re.compile(r"\d{2}/\d{2}/\d{4}([ ,]*\d{1,2}:\d{2}(:\d{2})?)?"), "DATE"),
    (re.compile(r"\d{4}-\d{2}-\d{2}[T ]\d{2}:\d{2}(:\d{2})?"), "DATE"),
    (re.compile(r"\d{1,2}:\d{2}:\d{2}"), "TIME"),
    (re.compile(r"[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", re.I), "UUID"),
    (re.compile(r"\b\d{10,}\b"), "NUM"),
    (re.compile(r"\d+\s*(milliseconds|millisecondes|ms)\b", re.I), r"N \1"),
    # compteurs d'etat applicatif, sans rapport avec l'echappement
    (re.compile(r"(en cache|in cache|objets?|objects?)\s*:?\s*\d+", re.I), r"\1 N"),
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
    # les <script> contiennent des < et > qui ne sont pas des balises : on les sort du flux
    scripts = []
    text = re.sub(r"<script\b[\s\S]*?</script>",
                  lambda m: scripts.append(re.sub(r"\s+", " ", m.group(0))) or "\x00SCRIPT\x00", text)
    text = re.sub(r"<!--.*?-->", "", text, flags=re.S)
    text = sort_attributes(text)
    text = re.sub(r"\s+", " ", text)
    # white space inside an attribute value is not significant for class lists
    text = re.sub(r'="\s*([^"]*?)\s*"', lambda m: '="%s"' % m.group(1), text)
    text = re.sub(r"\s+>", ">", text)
    text = re.sub(r">\s*<", ">\n<", text)
    for s_ in scripts:
        text = text.replace("\x00SCRIPT\x00", "\n" + s_ + "\n", 1)

    return [line.strip() for line in text.split("\n") if line.strip()]


def main():
    root = os.path.join(os.path.dirname(os.path.abspath(__file__)), "snapshots")
    left = sys.argv[1] if len(sys.argv) > 1 else os.path.join(root, "before")
    right = sys.argv[2] if len(sys.argv) > 2 else os.path.join(root, "after")

    if not os.path.isdir(left) or not os.path.isdir(right):
        print("  missing snapshot directory — both runs must exist")
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
        print("  %d page(s) differ between the two runs" % failed)
        return 1

    print("  every page renders identically in both runs")
    return 0


if __name__ == "__main__":
    sys.exit(main())
