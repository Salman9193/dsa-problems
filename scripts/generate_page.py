#!/usr/bin/env python3
"""
Generate a single-page HTML revision site from the dsa-problems repo.
Reads all Solution.java, NOTES.md, USE_CASES.md files and renders them
into a searchable, tabbed, syntax-highlighted HTML page.
"""

import os, re, pathlib

try:
    from pygments import highlight
    from pygments.lexers import JavaLexer
    from pygments.formatters import HtmlFormatter
    java_formatter = HtmlFormatter(style="monokai", cssclass="code-block", linenos=False)
    JAVA_CSS = java_formatter.get_style_defs(".code-block")
    def hl_java(code):
        return highlight(code, JavaLexer(), java_formatter)
except ImportError:
    JAVA_CSS = ""
    def hl_java(code):
        return f"<pre class='code-block'><code>{esc(code)}</code></pre>"

ROOT = pathlib.Path(".")
SITE = pathlib.Path("site")
SITE.mkdir(exist_ok=True)

TOPICS = [
    "arrays","strings","linked-list","binary-search",
    "dynamic-programming","graphs","trees","stacks",
    "bit-manipulation","design"
]

def esc(s):
    return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")

def md_to_html(text):
    """Lightweight Markdown to HTML converter."""
    # fenced code blocks
    def replace_fence(m):
        lang = m.group(1).strip()
        code = m.group(2)
        if lang in ("java","Java"):
            return hl_java(code)
        return "<pre class='code-block'><code>" + esc(code) + "</code></pre>"
    text = re.sub(r"```(\w*)\n(.*?)```", replace_fence, text, flags=re.DOTALL)
    # inline code
    text = re.sub(r"`([^`\n]+)`", lambda m: "<code class='inline'>" + esc(m.group(1)) + "</code>", text)
    # headers
    for i in range(6, 0, -1):
        text = re.sub(r"^#{" + str(i) + r"} (.+)$",
                      lambda m, i=i: f"<h{i}>{m.group(1)}</h{i}>",
                      text, flags=re.MULTILINE)
    # bold
    text = re.sub(r"\*\*(.+?)\*\*", r"<strong>\1</strong>", text)
    # italic
    text = re.sub(r"\*([^*\n]+?)\*", r"<em>\1</em>", text)
    # hr
    text = re.sub(r"^---+$", "<hr>", text, flags=re.MULTILINE)
    # tables
    lines = text.split("\n")
    out = []
    i = 0
    while i < len(lines):
        line = lines[i]
        if line.startswith("|") and line.endswith("|"):
            cells = [c.strip() for c in line[1:-1].split("|")]
            is_sep = all(re.match(r"^[-:]+$", c) for c in cells if c)
            if is_sep:
                i += 1
                continue
            tag = "th" if (i+1 < len(lines) and lines[i+1].startswith("|") and
                           all(re.match(r"^[-:| ]+$", lines[i+1]))) else "td"
            row = "".join(f"<{tag}>{esc(c)}</{tag}>" for c in cells)
            out.append(f"<tr>{row}</tr>")
        else:
            out.append(line)
        i += 1
    text = "\n".join(out)
    # wrap tables
    text = re.sub(r"(<tr>.*?</tr>\n?)+",
                  lambda m: f"<table class='md-table'>{m.group(0)}</table>",
                  text, flags=re.DOTALL)
    # lists
    text = re.sub(r"^[ \t]*[-*] (.+)$", r"<li>\1</li>", text, flags=re.MULTILINE)
    text = re.sub(r"(<li>.*?</li>\n?)+",
                  lambda m: f"<ul>{m.group(0)}</ul>", text, flags=re.DOTALL)
    # links
    text = re.sub(r"\[([^\]]+)\]\(([^)]+)\)",
                  r'<a href="\2" target="_blank">\1</a>', text)
    # paragraphs
    parts = re.split(r"\n{2,}", text)
    html_out = []
    for p in parts:
        p = p.strip()
        if not p:
            continue
        if re.match(r"^<(h[1-6]|ul|ol|pre|table|hr|tr)", p):
            html_out.append(p)
        else:
            html_out.append("<p>" + p.replace("\n", " ") + "</p>")
    return "\n".join(html_out)

# ── Collect problems ──────────────────────────────────────────────────────────
problems = []
for topic in TOPICS:
    td = ROOT / topic
    if not td.exists():
        continue
    for pd in sorted(td.iterdir()):
        if not pd.is_dir():
            continue
        sol   = pd / "Solution.java"
        notes = pd / "NOTES.md"
        uses  = pd / "USE_CASES.md"
        if not sol.exists():
            continue
        problems.append({
            "id":    f"{topic}--{pd.name}",
            "topic": topic,
            "name":  pd.name.replace("-"," ").title(),
            "sol":   sol.read_text(errors="replace") if sol.exists() else "",
            "notes": notes.read_text(errors="replace") if notes.exists() else "",
            "uses":  uses.read_text(errors="replace") if uses.exists() else "",
        })

# ── Collect guides ────────────────────────────────────────────────────────────
guides = []
guides_dir = ROOT / "guides"
if guides_dir.exists():
    for gf in sorted(guides_dir.glob("*.md")):
        guides.append({
            "id":      "guide--" + gf.stem.lower(),
            "name":    gf.stem.replace("_"," ").title(),
            "content": gf.read_text(errors="replace"),
        })

# ── Build sidebar ─────────────────────────────────────────────────────────────
sidebar_topics = {}
for p in problems:
    sidebar_topics.setdefault(p["topic"], []).append(p)

def sidebar_section(label, items, click_fn):
    rows = "".join(
        f'<div class="sitem" id="nav-{item["id"]}" onclick="{click_fn}(\'{item["id"]}\')">'
        f'{item["name"]}</div>'
        for item in items
    )
    n = len(items)
    return (f'<div class="stopic">'
            f'<div class="sheader" onclick="toggleSection(this)">'
            f'<span class="sicon">▶</span> {label} ({n})</div>'
            f'<div class="sbody">{rows}</div></div>')

sidebar_html = ""
for t, probs in sidebar_topics.items():
    sidebar_html += sidebar_section(t.replace("-"," ").title(), probs, "show")
sidebar_html += sidebar_section("Guides", guides, "show")

# ── Build panels ──────────────────────────────────────────────────────────────
panels_html = ""
for p in problems:
    sol_hl   = hl_java(p["sol"])
    notes_hl = md_to_html(p["notes"])
    uses_hl  = md_to_html(p["uses"])
    raw_id   = "raw--" + p["id"]
    panels_html += f"""
<div class="panel" id="panel-{p['id']}">
  <div class="phead">
    <span class="breadcrumb">{p['topic'].replace('-',' ').title()}</span>
    <h2>{p['name']}</h2>
  </div>
  <div class="tabs">
    <button class="tab active" onclick="switchTab(this,'tc-sol-{p['id']}')">Solution.java</button>
    <button class="tab" onclick="switchTab(this,'tc-notes-{p['id']}')">Notes</button>
    <button class="tab" onclick="switchTab(this,'tc-uses-{p['id']}')">Use Cases</button>
  </div>
  <div class="tc active" id="tc-sol-{p['id']}">
    <div class="copybar">
      <button class="copybtn" onclick="copyRaw('{raw_id}',this)">Copy</button>
    </div>
    <textarea id="{raw_id}" style="display:none">{esc(p['sol'])}</textarea>
    {sol_hl}
  </div>
  <div class="tc" id="tc-notes-{p['id']}"><div class="mdc">{notes_hl}</div></div>
  <div class="tc" id="tc-uses-{p['id']}"><div class="mdc">{uses_hl}</div></div>
</div>"""

for g in guides:
    panels_html += f"""
<div class="panel" id="panel-{g['id']}">
  <div class="phead">
    <span class="breadcrumb">Guide</span>
    <h2>{g['name']}</h2>
  </div>
  <div class="mdc guide-full">{md_to_html(g['content'])}</div>
</div>"""

# ── Topic cards ───────────────────────────────────────────────────────────────
topic_cards = ""
for t, probs in sidebar_topics.items():
    topic_cards += (f'<div class="tcard" onclick="filterTopic(\'{t}\')">'
                    f'<div class="tcount">{len(probs)}</div>'
                    f'<div class="tlabel">{t.replace("-"," ").title()}</div></div>')
topic_cards += (f'<div class="tcard" onclick="filterTopic(\'guides\')">'
                f'<div class="tcount">{len(guides)}</div>'
                f'<div class="tlabel">Guides</div></div>')

# ── Assemble ──────────────────────────────────────────────────────────────────
html = f"""<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>DSA Revision — Staff/Principal Engineer</title>
<style>
*,*::before,*::after{{box-sizing:border-box;margin:0;padding:0}}
:root{{
  --bg:#0f1117;--surf:#1a1d27;--surf2:#22253a;--bord:#2e3250;
  --acc:#6c8fff;--acc2:#a78bfa;--txt:#e2e8f0;--muted:#8892b0;
  --green:#4ade80;--sw:280px;--hh:56px
}}
body{{font-family:'Segoe UI',system-ui,sans-serif;background:var(--bg);
      color:var(--txt);display:flex;flex-direction:column;height:100vh;overflow:hidden}}

/* Header */
.hdr{{height:var(--hh);background:var(--surf);border-bottom:1px solid var(--bord);
      display:flex;align-items:center;padding:0 20px;gap:16px;flex-shrink:0;z-index:100}}
.hlogo{{font-size:20px;font-weight:700;background:linear-gradient(135deg,var(--acc),var(--acc2));
        -webkit-background-clip:text;-webkit-text-fill-color:transparent}}
.hsub{{color:var(--muted);font-size:13px}}
.swrap{{flex:1;max-width:420px;margin-left:auto}}
#search{{width:100%;background:var(--surf2);border:1px solid var(--bord);color:var(--txt);
         padding:8px 14px;border-radius:8px;font-size:14px;outline:none}}
#search:focus{{border-color:var(--acc);box-shadow:0 0 0 3px rgba(108,143,255,.15)}}
.badges{{display:flex;gap:8px}}
.badge{{background:var(--surf2);border:1px solid var(--bord);border-radius:20px;
        padding:3px 10px;font-size:12px;color:var(--muted)}}
.badge span{{color:var(--acc);font-weight:600}}

/* Layout */
.layout{{display:flex;flex:1;overflow:hidden}}

/* Sidebar */
.sidebar{{width:var(--sw);background:var(--surf);border-right:1px solid var(--bord);
          overflow-y:auto;flex-shrink:0}}
.sidebar::-webkit-scrollbar{{width:4px}}
.sidebar::-webkit-scrollbar-thumb{{background:var(--bord);border-radius:2px}}
.stopic{{border-bottom:1px solid var(--bord)}}
.sheader{{padding:9px 16px;cursor:pointer;font-size:11px;font-weight:700;
          text-transform:uppercase;letter-spacing:.06em;color:var(--muted);
          display:flex;align-items:center;gap:8px;user-select:none;transition:background .15s}}
.sheader:hover{{background:var(--surf2);color:var(--txt)}}
.sicon{{font-size:9px;transition:transform .2s;display:inline-block}}
.sheader.open .sicon{{transform:rotate(90deg)}}
.sbody{{display:none}}
.sbody.open{{display:block}}
.sitem{{padding:7px 16px 7px 26px;cursor:pointer;font-size:13px;color:var(--muted);
        transition:all .15s;border-left:2px solid transparent}}
.sitem:hover{{background:var(--surf2);color:var(--txt)}}
.sitem.active{{background:rgba(108,143,255,.1);color:var(--acc);border-left-color:var(--acc)}}
.sitem.hidden{{display:none}}

/* Main */
.main{{flex:1;overflow-y:auto;padding:28px 36px}}
.main::-webkit-scrollbar{{width:7px}}
.main::-webkit-scrollbar-thumb{{background:var(--bord);border-radius:3px}}

/* Welcome */
.welcome{{text-align:center;padding:60px 20px}}
.welcome h1{{font-size:30px;margin-bottom:12px;
             background:linear-gradient(135deg,var(--acc),var(--acc2));
             -webkit-background-clip:text;-webkit-text-fill-color:transparent}}
.welcome p{{color:var(--muted);font-size:15px;margin-bottom:36px}}
.tgrid{{display:grid;grid-template-columns:repeat(auto-fill,minmax(150px,1fr));
        gap:12px;max-width:680px;margin:0 auto}}
.tcard{{background:var(--surf);border:1px solid var(--bord);border-radius:10px;
        padding:16px;cursor:pointer;transition:all .2s;text-align:center}}
.tcard:hover{{border-color:var(--acc);transform:translateY(-2px);
              box-shadow:0 4px 20px rgba(108,143,255,.2)}}
.tcount{{font-size:26px;font-weight:700;color:var(--acc)}}
.tlabel{{font-size:12px;color:var(--muted);margin-top:4px}}

/* Panel */
.panel{{display:none;animation:fi .2s}}
.panel.active{{display:block}}
@keyframes fi{{from{{opacity:0;transform:translateY(5px)}}to{{opacity:1;transform:none}}}}
.phead{{margin-bottom:20px}}
.breadcrumb{{font-size:11px;color:var(--muted);text-transform:uppercase;
             letter-spacing:.06em;display:block;margin-bottom:6px}}
.phead h2{{font-size:22px;font-weight:700}}

/* Tabs */
.tabs{{display:flex;border-bottom:1px solid var(--bord);margin-bottom:20px}}
.tab{{background:none;border:none;color:var(--muted);padding:9px 20px;cursor:pointer;
      font-size:14px;font-weight:500;border-bottom:2px solid transparent;
      transition:all .15s;margin-bottom:-1px}}
.tab:hover{{color:var(--txt)}}
.tab.active{{color:var(--acc);border-bottom-color:var(--acc)}}
.tc{{display:none}}
.tc.active{{display:block}}

/* Code */
.copybar{{display:flex;justify-content:flex-end;margin-bottom:8px}}
.copybtn{{background:var(--surf2);border:1px solid var(--bord);color:var(--muted);
          padding:4px 12px;border-radius:6px;cursor:pointer;font-size:12px;transition:all .15s}}
.copybtn:hover{{background:var(--acc);color:#fff;border-color:var(--acc)}}
.code-block{{border-radius:10px;overflow:auto;font-size:13px;line-height:1.6}}
.code-block pre{{padding:20px !important;background:#1a1b26 !important;margin:0;
                 border-radius:10px;overflow-x:auto}}
{JAVA_CSS}

/* Markdown */
.mdc{{max-width:860px;line-height:1.75}}
.mdc h1{{font-size:20px;color:var(--acc);margin:22px 0 10px}}
.mdc h2{{font-size:17px;color:var(--txt);margin:18px 0 9px;
         padding-bottom:6px;border-bottom:1px solid var(--bord)}}
.mdc h3{{font-size:14px;color:var(--acc2);margin:14px 0 7px}}
.mdc h4{{font-size:13px;color:var(--muted);margin:12px 0 6px;font-style:italic}}
.mdc p{{margin:9px 0;color:var(--txt)}}
.mdc ul{{padding-left:22px;margin:9px 0}}
.mdc li{{margin:4px 0;color:var(--muted)}}
.mdc strong{{color:var(--txt)}}
.mdc em{{color:var(--acc2)}}
.mdc hr{{border:none;border-top:1px solid var(--bord);margin:18px 0}}
.mdc a{{color:var(--acc);text-decoration:none}}
.mdc a:hover{{text-decoration:underline}}
.mdc pre.code-block,.mdc pre{{background:#1a1b26;border-radius:8px;
  padding:14px;overflow-x:auto;margin:12px 0;font-size:13px;line-height:1.5}}
.mdc code.inline{{background:var(--surf2);padding:2px 6px;border-radius:4px;
  font-size:12px;color:var(--acc2);font-family:monospace}}
.md-table{{width:100%;border-collapse:collapse;margin:12px 0;font-size:13px}}
.md-table td,.md-table th{{padding:8px 12px;border:1px solid var(--bord)}}
.md-table th{{background:var(--surf2);color:var(--txt);font-weight:600;text-align:left}}
.md-table td{{color:var(--muted)}}
.md-table tr:nth-child(even) td{{background:rgba(255,255,255,.02)}}
.guide-full{{max-width:900px}}
</style>
</head>
<body>

<div class="hdr">
  <div>
    <div class="hlogo">⚡ DSA Problems</div>
    <div class="hsub">Staff / Principal Engineer Revision</div>
  </div>
  <div class="swrap">
    <input type="text" id="search" placeholder="Search problems and guides..." oninput="doSearch(this.value)">
  </div>
  <div class="badges">
    <div class="badge"><span>{len(problems)}</span> Problems</div>
    <div class="badge"><span>{len(guides)}</span> Guides</div>
  </div>
</div>

<div class="layout">
  <div class="sidebar">{sidebar_html}</div>
  <div class="main">
    <div id="welcome" class="welcome">
      <h1>DSA Revision Hub</h1>
      <p>Click any problem in the sidebar, or search above.<br>
         Each problem has Solution, Notes &amp; Real-World Use Cases.</p>
      <div class="tgrid">{topic_cards}</div>
    </div>
    {panels_html}
  </div>
</div>

<script>
let cur=null, curNav=null;

function show(id){{
  document.getElementById('welcome').style.display='none';
  if(cur)cur.classList.remove('active');
  if(curNav)curNav.classList.remove('active');
  cur=document.getElementById('panel-'+id);
  curNav=document.getElementById('nav-'+id);
  if(cur)cur.classList.add('active');
  if(curNav){{curNav.classList.add('active');curNav.scrollIntoView({{block:'nearest'}});}}
  window.scrollTo(0,0);
}}

function switchTab(btn,tcId){{
  const panel=btn.closest('.panel');
  panel.querySelectorAll('.tab').forEach(t=>t.classList.remove('active'));
  panel.querySelectorAll('.tc').forEach(t=>t.classList.remove('active'));
  btn.classList.add('active');
  document.getElementById(tcId).classList.add('active');
}}

function toggleSection(hdr){{
  hdr.classList.toggle('open');
  hdr.nextElementSibling.classList.toggle('open');
}}

function filterTopic(topic){{
  document.querySelectorAll('.sheader').forEach(h=>{{
    const txt=h.textContent.trim().toLowerCase();
    const match=txt.includes(topic.replace('-',' ').toLowerCase())||
                (topic==='guides'&&txt.startsWith('guides'));
    if(match){{
      h.classList.add('open');
      h.nextElementSibling.classList.add('open');
      const first=h.nextElementSibling.querySelector('.sitem');
      if(first)first.click();
    }}
  }});
}}

function doSearch(q){{
  q=q.toLowerCase().trim();
  document.querySelectorAll('.sitem').forEach(item=>{{
    item.classList.toggle('hidden',q&&!item.textContent.toLowerCase().includes(q));
  }});
  if(q){{
    document.querySelectorAll('.sheader').forEach(h=>{{
      h.classList.add('open');
      h.nextElementSibling.classList.add('open');
    }});
  }}
}}

function copyRaw(rawId,btn){{
  const txt=document.getElementById(rawId).value;
  navigator.clipboard.writeText(txt).then(()=>{{
    btn.textContent='Copied!';btn.style.background='#4ade80';btn.style.color='#000';
    setTimeout(()=>{{btn.textContent='Copy';btn.style.background='';btn.style.color='';
    }},1500);
  }});
}}

// Open first section by default
document.querySelector('.sheader')&&document.querySelector('.sheader').click();
</script>
</body>
</html>"""

out = SITE / "index.html"
out.write_text(html, encoding="utf-8")
size_kb = out.stat().st_size // 1024
print(f"Generated site/index.html — {len(problems)} problems, {len(guides)} guides, ~{size_kb}KB")
