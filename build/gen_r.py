#!/usr/bin/env python3
"""Generate non-transitive library R classes for the Canteen Mitra build.

Inputs:
  ids.txt   : aapt2 --emit-ids output ("<pkg>:<type>/<name> = 0x7f..") from the
              merged link (app + all library resources)
  aar pkg/rtxt files: for each AAR, the package from AndroidManifest + its R.txt

Output:
  A Java source tree under OUT/libRgen/<...> for every library R class,
  compilable with javac against android.jar only.
"""
import os, sys, re

def main():
    base = sys.argv[1]          # ccapp/build
    ids_path = os.path.join(base, "ids.txt")
    gen_root = os.path.join(base, "libRgen")

    # ---- parse ids.txt: (pkg,type,name) -> int ; plus a per (type,name) global ----
    by_key = {}
    by_type_name = {}
    with open(ids_path) as f:
        for line in f:
            m = re.match(r"\s*([\w.]+):(\w+)/(\S+)\s*=\s*(0x[0-9a-fA-F]+)", line)
            if m:
                pkg, typ, name, val = m.group(1), m.group(2), m.group(3), int(m.group(4), 16)
                by_key[(pkg, typ, name)] = val
                by_type_name[(typ, name)] = val   # later entries win; deterministic enough

    def resolve(val, pkg, typ, name):
        if val == 0:
            v = by_key.get((pkg, typ, name))
            if v is None:
                v = by_type_name.get((typ, name))
            return v if v is not None else 0
        if 0x01000000 <= val < 0x02000000:      # framework 0x01xxxxxx — absolute
            return val
        # 0x7f value: re-resolve by name against our own ids table
        v = by_key.get((pkg, typ, name))
        if v is None:
            v = by_type_name.get((typ, name))
        return v if v is not None else val

    def fqn(pkg, cls):
        return pkg + "/" + cls if cls else pkg

    # ---- iterate library R.txt files ----
    packages = []
    for fn in sorted(os.listdir(base)):
        m = re.match(r"pkg_(.+)\.txt$", fn)
        if not m:
            continue
        aar = m.group(1)
        rtxt = os.path.join(base, "rtxt_" + aar + ".txt")
        if not os.path.exists(rtxt):
            continue
        pkg = open(os.path.join(base, fn)).read().strip()
        if not pkg or pkg.startswith("android.") or pkg == "android":
            continue
        packages.append((aar, pkg, rtxt))

    written = 0
    for aar, pkg, rtxt in packages:
        # types -> {name: value}  ; styleable arrays: {name: [vals]}; styleable index fields
        fields = {}
        styleable_arrays = {}
        styleable_index = {}  # (arrayname, index) -> fieldname
        with open(rtxt) as f:
            for line in f:
                line = line.strip()
                if not line:
                    continue
                m = re.match(r"int\[\] styleable (\S+) \{(.*)\}", line)
                if m:
                    arr = m.group(1)
                    raw = m.group(2).replace(",", " ").split()
                    vals = [int(x, 16) if x.startswith("0x") else int(x, 0) for x in raw if x]
                    styleable_arrays[arr] = vals
                    continue
                m = re.match(r"int styleable (\S+)_(\S+) (\d+)", line)
                if m:
                    styleable_index[(m.group(1), int(m.group(3)))] = m.group(2)
                    continue
                m = re.match(r"int (\w+) (\S+) (0x[0-9a-fA-F]+)$", line)
                if m:
                    typ, name, val = m.group(1), m.group(2), int(m.group(3), 16)
                    fields.setdefault(typ, {})[name] = val

        if not fields and not styleable_arrays:
            continue

        # resolve
        for typ, d in fields.items():
            for name in list(d):
                d[name] = resolve(d[name], pkg, typ, name)
        # styleable: index fields keep 0-based ordinal; arrays resolve element-by-element
        for arr, vals in styleable_arrays.items():
            out = []
            for i, v in enumerate(vals):
                if v == 0:
                    suffix = styleable_index.get((arr, i))
                    out.append(resolve(0, pkg, "attr", suffix) if suffix else 0)
                elif 0x01000000 <= v < 0x02000000:
                    out.append(v)
                else:
                    suffix = styleable_index.get((arr, i))
                    out.append(resolve(v, pkg, "attr", suffix) if suffix else v)
            styleable_arrays[arr] = out

        # emit java
        path = os.path.join(gen_root, *pkg.split("."), "R.java")
        os.makedirs(os.path.dirname(path), exist_ok=True)
        lines = ["package %s;" % pkg, "", "public final class R {"]
        # stable ordering: emit non-styleable types first
        order = ["bool","attr","color","dimen","drawable","id","integer","layout","plurals","string","style","xml","menu"]
        for typ in order:
            if typ in fields and fields[typ]:
                lines.append("  public static final class %s {" % typ)
                for name, v in sorted(fields[typ].items()):
                    lines.append("    public static final int %s = 0x%08x;" % (name, v))
                lines.append("  }")
        if styleable_arrays:
            lines.append("  public static final class styleable {")
            # index (ordinal) fields
            for (arr, i), suffix in sorted(styleable_index.items()):
                lines.append("    public static final int %s_%s = %d;" % (arr, suffix, i))
            # int[] arrays
            for arr in sorted(styleable_arrays):
                vals = ", ".join("0x%08x" % v for v in styleable_arrays[arr])
                lines.append("    public static final int[] %s = { %s };" % (arr, vals))
            lines.append("  }")
        lines.append("}")
        with open(path, "w") as f:
            f.write("\n".join(lines) + "\n")
        written += 1
    print("generated R classes for %d packages -> %s" % (written, gen_root))

if __name__ == "__main__":
    main()
