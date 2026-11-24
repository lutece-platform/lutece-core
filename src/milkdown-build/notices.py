"""Writes THIRD-PARTY-NOTICES.txt of the Milkdown bundle of the back office from the esbuild metafile (see README.md).

usage: python3 notices.py <build folder> <THIRD-PARTY-NOTICES.txt>

The build folder holds meta.json (esbuild --metafile) and the node_modules of the build. Every package with a file in the
bundle is listed, also those whose code was dropped by tree shaking but whose module is still in the output (re-exports):
one line per package (name, version, license), then the license file of each package.
"""
import json
import os
import re
import sys

HEADER = ('Third-party software bundled in milkdown.min.js and milkdown.min.css (built from src/milkdown-build/entry.js of '
          'lutece-core, see src/milkdown-build/README.md)')
PACKAGE_DIR = re.compile(r'(.*node_modules/(?:@[^/]+/)?[^/]+)/')
LICENSE_FILE = re.compile(r'(?i)licen[cs]e')


def bundled_packages(build):
    with open(os.path.join(build, 'meta.json'), encoding='utf-8') as f:
        meta = json.load(f)
    inputs = {path for output in meta['outputs'].values() for path in output['inputs']}
    packages = {}
    for path in sorted(inputs):
        match = PACKAGE_DIR.match(path)
        if not match:
            continue
        folder = os.path.join(build, match.group(1))
        with open(os.path.join(folder, 'package.json'), encoding='utf-8') as f:
            package = json.load(f)
        license_name = package.get('license') or ''
        if isinstance(license_name, dict):
            license_name = license_name.get('type', '')
        packages[(package['name'], package['version'])] = (license_name, folder)
    return packages


def license_text(folder):
    files = sorted(name for name in os.listdir(folder) if LICENSE_FILE.match(name))
    if not files:
        return '(no license file in the package)'
    with open(os.path.join(folder, files[0]), encoding='utf-8') as f:
        return f.read().strip()


def main():
    build, out = sys.argv[1:3]
    packages = bundled_packages(build)
    keys = sorted(packages)
    text = '\n'.join([HEADER, ''] + ['%s %s — %s' % (name, version, packages[(name, version)][0]) for name, version in keys]) + '\n'
    for name, version in keys:
        license_name, folder = packages[(name, version)]
        text += '\n\n==== %s %s (%s) ====\n\n%s\n' % (name, version, license_name, license_text(folder))
    with open(out, 'w', encoding='utf-8') as f:
        f.write(text)
    print(len(keys), 'packages')


if __name__ == '__main__':
    main()
