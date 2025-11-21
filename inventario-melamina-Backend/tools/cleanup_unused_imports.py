#!/usr/bin/env python3
import os
import re
import json

root = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
paths = [os.path.join(root, 'src', 'main', 'java'), os.path.join(root, 'src', 'test', 'java')]

unused_report = {}

for base in paths:
    if not os.path.isdir(base):
        continue
    for dirpath, _, filenames in os.walk(base):
        for fn in filenames:
            if not fn.endswith('.java'):
                continue
            fp = os.path.join(dirpath, fn)
            try:
                with open(fp, 'r', encoding='utf-8') as f:
                    text = f.read()
            except Exception as e:
                print(f"Failed reading {fp}: {e}")
                continue
            lines = text.splitlines()
            imports = []
            import_lines_idx = []
            for i, line in enumerate(lines):
                s = line.strip()
                if s.startswith('import '):
                    imports.append(s)
                    import_lines_idx.append(i)
            if not imports:
                continue
            # Build searchable body excluding import lines
            body_lines = [l for idx, l in enumerate(lines) if idx not in import_lines_idx]
            body = "\n".join(body_lines)

            unused = []
            # remove comments once to speed checks
            body_no_comments = re.sub(r'/\*.*?\*/', '', body, flags=re.S)
            body_no_comments = re.sub(r'//.*', '', body_no_comments)

            for imp in imports:
                # skip wildcard imports
                if imp.endswith('.*;'):
                    continue
                # remove 'import' and trailing ';'
                imp_inner = imp[len('import '):].rstrip(';').strip()
                # handle static
                if imp_inner.startswith('static '):
                    imp_inner = imp_inner[len('static '):].strip()
                # get simple name
                simple = imp_inner.split('.')[-1]
                # if the simple name appears as word, consider used
                if not re.search(r"\b{}\b".format(re.escape(simple)), body_no_comments):
                    unused.append(imp)
            if unused:
                rel = os.path.relpath(fp, root)
                unused_report[rel] = unused

# Print human readable
if not unused_report:
    print('No unused imports detected by heuristic.')
else:
    print(json.dumps(unused_report, indent=2, ensure_ascii=False))

# Also write to file
outp = os.path.join(root, 'tools', 'unused_imports_report.json')
with open(outp, 'w', encoding='utf-8') as f:
    json.dump(unused_report, f, indent=2, ensure_ascii=False)

print('\nReport written to', outp)
