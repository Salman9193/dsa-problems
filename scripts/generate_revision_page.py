#!/usr/bin/env python3
# Generates a single-page HTML revision site from the DSA problems repo.
# Outputs: ./site/index.html
import re, html
from pathlib import Path

TOPICS = [
    ("arrays",               "Arrays & Two Pointers"),
    ("strings",              "Strings"),
    ("linked-list",          "Linked List"),
    ("binary-search",        "Binary Search"),
    ("stacks",               "Stacks"),
    ("dynamic-programming",  "Dynamic Programming & Backtracking"),
    ("trees",                "Trees"),
    ("graphs",               "Graphs"),
    ("design",               "Design"),
    ("bit-manipulation",     "Bit Manipulation"),
    ("guides",               "Concept Guides"),
]

ROOT = Path(".")
SITE = Path("site")
SITE.mkdir(exist_ok=True)

def slugify(s):
    return re.sub(r"[^a-z0-9-]", "-", s.lower()).strip("-")

def read_file(path):
    try:
        return path.read_text(encoding="utf-8")
    except Exception:
        return ""

def esc(s):
    return html.escape(str(s), quote=True)

JAVA_KW = {
    "public","private","protected","static","final","class","interface",
    "void","int","long","boolean","char","String","return","new","if","else",
    "for","while","break","continue","null","true","false","this","import",
    "List","Map","Set","Queue","Deque","Arrays","Collections","ArrayList",
    "HashMap","HashSet","LinkedList","ArrayDeque","PriorityQueue","TreeMap",
    "TreeSet","Integer","Math","throws","throw","try","catch","finally",
    "extends","implements",
}


LC = {
    "two-sum-ii":(167,"two-sum-ii-input-array-is-sorted"),
    "3sum":(15,"3sum"),
    "container-with-most-water":(11,"container-with-most-water"),
    "trapping-rain-water":(42,"trapping-rain-water"),
    "product-of-array-except-self":(238,"product-of-array-except-self"),
    "find-pivot-index":(724,"find-pivot-index"),
    "sliding-window-maximum":(239,"sliding-window-maximum"),
    "car-pooling":(1094,"car-pooling"),
    "longest-consecutive-sequence":(128,"longest-consecutive-sequence"),
    "group-anagrams":(49,"group-anagrams"),
    "top-k-frequent-elements":(347,"top-k-frequent-elements"),
    "divide-two-integers":(29,"divide-two-integers"),
    "minimum-cost-hire-k-workers":(857,"minimum-cost-to-hire-k-workers"),
    "best-time-to-buy-and-sell-stock":(121,"best-time-to-buy-and-sell-stock"),
    "best-time-to-buy-and-sell-stock-ii":(122,"best-time-to-buy-and-sell-stock-ii"),
    "best-time-to-buy-and-sell-stock-iii":(123,"best-time-to-buy-and-sell-stock-iii"),
    "best-time-to-buy-and-sell-stock-iv":(188,"best-time-to-buy-and-sell-stock-iv"),
    "make-two-arrays-equal":(1460,"make-two-arrays-equal-by-reversing"),
    "minimum-time-visiting-all-points":(1266,"minimum-time-visiting-all-points"),
    "merge-intervals":(56,"merge-intervals"),
    "meeting-rooms-ii":(253,"meeting-rooms-ii"),
    "kth-largest-element":(215,"kth-largest-element-in-an-array"),
    "valid-palindrome":(125,"valid-palindrome"),
    "longest-palindromic-substring":(5,"longest-palindromic-substring"),
    "longest-substring-without-repeating":(3,"longest-substring-without-repeating-characters"),
    "longest-repeating-character-replacement":(424,"longest-repeating-character-replacement"),
    "minimum-window-substring":(76,"minimum-window-substring"),
    "find-all-anagrams":(438,"find-all-anagrams-in-a-string"),
    "first-unique-character":(387,"first-unique-character-in-a-string"),
    "encode-and-decode-strings":(271,"encode-and-decode-strings"),
    "decode-string":(394,"decode-string"),
    "minimum-remove-valid-parentheses":(1249,"minimum-remove-to-make-valid-parentheses"),
    "implement-trie":(208,"implement-trie-prefix-tree"),
    "reverse-linked-list":(206,"reverse-linked-list"),
    "linked-list-cycle":(141,"linked-list-cycle"),
    "linked-list-random-node":(382,"linked-list-random-node"),
    "search-in-rotated-sorted-array":(33,"search-in-rotated-sorted-array"),
    "find-in-mountain-array":(1095,"find-in-mountain-array"),
    "sqrt-x":(69,"sqrtx"),
    "online-election":(911,"online-election"),
    "valid-parentheses":(20,"valid-parentheses"),
    "daily-temperatures":(739,"daily-temperatures"),
    "largest-rectangle-histogram":(84,"largest-rectangle-in-histogram"),
    "climbing-stairs":(70,"climbing-stairs"),
    "coin-change":(322,"coin-change"),
    "word-break":(139,"word-break"),
    "house-robber":(198,"house-robber"),
    "jump-game":(55,"jump-game"),
    "unique-paths":(62,"unique-paths"),
    "cherry-pickup-ii":(1463,"cherry-pickup-ii"),
    "number-of-ways-paint-grid":(1411,"number-of-ways-to-paint-n-3-grid"),
    "partition-equal-subset-sum":(416,"partition-equal-subset-sum"),
    "longest-increasing-subsequence":(300,"longest-increasing-subsequence"),
    "burst-balloons":(312,"burst-balloons"),
    "subsets":(78,"subsets"),
    "permutations":(46,"permutations"),
    "combination-sum":(39,"combination-sum"),
    "word-search":(79,"word-search"),
    "populating-next-right-pointers-ii":(117,"populating-next-right-pointers-in-each-node-ii"),
    "delete-nodes-and-return-forest":(1110,"delete-nodes-and-return-forest"),
    "lca-deepest-leaves":(1123,"lowest-common-ancestor-of-deepest-leaves"),
    "all-possible-full-binary-trees":(894,"all-possible-full-binary-trees"),
    "binary-search-tree-iterator":(173,"binary-search-tree-iterator"),
    "lowest-common-ancestor":(236,"lowest-common-ancestor-of-a-binary-tree"),
    "binary-tree-max-path-sum":(124,"binary-tree-maximum-path-sum"),
    "validate-bst":(98,"validate-binary-search-tree"),
    "clone-graph":(133,"clone-graph"),
    "find-if-path-exists":(1971,"find-if-path-exists-in-graph"),
    "number-of-islands":(200,"number-of-islands"),
    "rotting-oranges":(994,"rotting-oranges"),
    "01-matrix":(542,"01-matrix"),
    "shortest-path-binary-matrix":(1091,"shortest-path-in-binary-matrix"),
    "word-ladder":(127,"word-ladder"),
    "max-area-of-island":(695,"max-area-of-island"),
    "pacific-atlantic-water-flow":(417,"pacific-atlantic-water-flow"),
    "surrounded-regions":(130,"surrounded-regions"),
    "number-of-provinces":(547,"number-of-provinces"),
    "course-schedule":(207,"course-schedule"),
    "course-schedule-ii":(210,"course-schedule-ii"),
    "find-eventual-safe-states":(802,"find-eventual-safe-states"),
    "alien-dictionary":(269,"alien-dictionary"),
    "is-graph-bipartite":(785,"is-graph-bipartite"),
    "possible-bipartition":(886,"possible-bipartition"),
    "redundant-connection":(684,"redundant-connection"),
    "accounts-merge":(721,"accounts-merge"),
    "most-stones-removed":(947,"most-stones-removed-with-same-row-or-column"),
    "evaluate-division":(399,"evaluate-division"),
    "network-delay-time":(743,"network-delay-time"),
    "path-minimum-effort":(1631,"path-with-minimum-effort"),
    "cheapest-flights-k-stops":(787,"cheapest-flights-within-k-stops"),
    "swim-in-rising-water":(778,"swim-in-rising-water"),
    "min-cost-connect-points":(1584,"min-cost-to-connect-all-points"),
    "connecting-cities-min-cost":(1135,"connecting-cities-with-minimum-cost"),
    "critical-connections":(1192,"critical-connections-in-a-network"),
    "minimum-height-trees":(310,"minimum-height-trees"),
    "lru-cache":(146,"lru-cache"),
    "exam-room":(855,"exam-room"),
    "find-median-data-stream":(295,"find-median-from-data-stream"),
    "hamming-weight":(191,"number-of-1-bits"),
}


def highlight_code_token(text):
    """
    Highlight a plain-code token (no comments/strings inside).
    Scan character-by-character to emit: identifiers, numbers, punctuation.
    Never runs a second regex pass over already-emitted HTML.
    """
    out = []
    i = 0
    n = len(text)
    while i < n:
        c = text[i]
        # Identifier or keyword
        if c.isalpha() or c == '_':
            j = i
            while j < n and (text[j].isalnum() or text[j] == '_'):
                j += 1
            word = text[i:j]
            if word in JAVA_KW:
                out.append(f"<span class='kw'>{word}</span>")
            else:
                out.append(html.escape(word, quote=False))
            i = j
        # Number literal
        elif c.isdigit():
            j = i
            while j < n and (text[j].isdigit() or text[j] in '.xXaAbBcCdDeEfFLl_'):
                j += 1
            out.append(f"<span class='nm'>{html.escape(text[i:j], quote=False)}</span>")
            i = j
        # Any other character — escape and emit
        else:
            out.append(html.escape(c, quote=False))
            i += 1
    return "".join(out)


def highlight_java(raw_code):
    """
    Tokenise Java source into comments / string literals / plain code,
    then syntax-highlight only the plain-code tokens.
    No regex ever runs over already-emitted HTML.
    """
    out = []
    i = 0
    s = raw_code
    n = len(s)

    while i < n:
        # Block comment
        if s[i:i+2] == "/*":
            end = s.find("*/", i+2)
            end = end + 2 if end != -1 else n
            out.append("<span class='cm'>" + html.escape(s[i:end], quote=False) + "</span>")
            i = end

        # Line comment
        elif s[i:i+2] == "//":
            end = s.find("\n", i)
            end = end if end != -1 else n
            out.append("<span class='cm'>" + html.escape(s[i:end], quote=False) + "</span>")
            i = end

        # String literal
        elif s[i] == '"':
            j = i + 1
            while j < n:
                if s[j] == '\\':   j += 2
                elif s[j] == '"':  j += 1; break
                else:              j += 1
            out.append("<span class='st'>" + html.escape(s[i:j], quote=False) + "</span>")
            i = j

        # Char literal
        elif s[i] == "'":
            j = i + 1
            while j < n:
                if s[j] == '\\':   j += 2
                elif s[j] == "'":  j += 1; break
                else:              j += 1
            out.append("<span class='st'>" + html.escape(s[i:j], quote=False) + "</span>")
            i = j

        # Plain code — accumulate until next special boundary
        else:
            j = i
            while j < n and s[j:j+2] not in ("//", "/*") and s[j] not in ('"', "'"):
                j += 1
            if j > i:
                out.append(highlight_code_token(s[i:j]))
            i = j

    return "".join(out)


def inline_md(text):
    def code_sub(m):
        return "<code>" + esc(m.group(1)) + "</code>"
    text = re.sub(r"`([^`]+)`", code_sub, text)
    text = re.sub(r"\*\*([^*]+)\*\*", r"<strong>\1</strong>", text)
    text = re.sub(r"\*([^*]+)\*", r"<em>\1</em>", text)
    text = re.sub(
        r"\[([^\]]+)\]\(([^)]+)\)",
        lambda m: f'<a href="{esc(m.group(2))}" target="_blank">{esc(m.group(1))}</a>',
        text
    )
    return text


def md_to_html(text):
    lines = text.split("\n")
    out = []
    in_code = False
    code_buf = []
    code_lang = ""
    in_table = False
    had_th = False

    for line in lines:
        if line.startswith("```"):
            if in_code:
                raw = "\n".join(code_buf)
                body = highlight_java(raw) if code_lang.lower() in ("java","") else html.escape(raw, quote=False)
                out.append(f"<pre><code>{body}</code></pre>")
                code_buf = []; code_lang = ""; in_code = False
                in_table = False; had_th = False
            else:
                in_code = True; code_lang = line[3:].strip()
            continue

        if in_code:
            code_buf.append(line); continue

        if line.startswith("|"):
            if not in_table:
                out.append("<table>"); in_table = True; had_th = False
            if re.match(r"^\|[-| :]+\|$", line): continue
            cells = [c.strip() for c in line.strip("|").split("|")]
            tag = "th" if not had_th else "td"
            if not had_th: had_th = True
            out.append("<tr>" + "".join(f"<{tag}>{inline_md(c)}</{tag}>" for c in cells) + "</tr>")
            continue
        else:
            if in_table:
                out.append("</table>"); in_table = False; had_th = False

        m = re.match(r"^(#{1,4})\s+(.*)", line)
        if m:
            lvl = min(len(m.group(1)) + 1, 6)
            out.append(f"<h{lvl}>{inline_md(m.group(2))}</h{lvl}>"); continue
        if re.match(r"^-{3,}$", line.strip()):
            out.append("<hr>"); continue
        m = re.match(r"^\s*[-*]\s+(.*)", line)
        if m: out.append(f"<li>{inline_md(m.group(1))}</li>"); continue
        m = re.match(r"^\s*\d+\.\s+(.*)", line)
        if m: out.append(f"<li>{inline_md(m.group(1))}</li>"); continue
        if not line.strip(): out.append("<br>"); continue
        out.append(f"<p>{inline_md(line)}</p>")

    if in_table: out.append("</table>")
    return "\n".join(out)


def build_problem_card(prob_dir, topic_slug):
    name    = prob_dir.name
    display = name.replace("-", " ").title()
    cid     = f"{topic_slug}-{name}"
    sol     = read_file(prob_dir / "Solution.java")
    notes   = read_file(prob_dir / "NOTES.md")
    uses    = read_file(prob_dir / "USE_CASES.md")
    diff = "Medium"
    if "| Easy" in notes or "Easy |" in notes: diff = "Easy"
    elif "| Hard" in notes or "Hard |" in notes: diff = "Hard"
    tabs = ""; panels = ""
    if sol:
        tabs   += f"<button class='tab-btn active' onclick=\"switchTab(event,'{cid}-sol')\">Solution.java</button>"
        panels += f"<div id='{cid}-sol' class='tab-panel active'><pre><code>{highlight_java(sol)}</code></pre></div>"
    if notes:
        tabs   += f"<button class='tab-btn' onclick=\"switchTab(event,'{cid}-notes')\">Notes</button>"
        panels += f"<div id='{cid}-notes' class='tab-panel'>{md_to_html(notes)}</div>"
    if uses:
        tabs   += f"<button class='tab-btn' onclick=\"switchTab(event,'{cid}-uses')\">Use Cases</button>"
        panels += f"<div id='{cid}-uses' class='tab-panel'>{md_to_html(uses)}</div>"
    return (
        f"<div class='card' id='{cid}' data-topic='{topic_slug}' data-name='{esc(display.lower())}'>"
        f"<div class='card-header' onclick=\"toggleCard('{cid}')\">"
        f"<span class='card-title'>{esc(display)}</span>"
        + (f"<a class='lc-link' href='https://leetcode.com/problems/{LC[name][1]}/' "
           f"target='_blank' onclick='event.stopPropagation()'>#{LC[name][0]}</a>"
           if name in LC else "")
        + f"<span class='badge {diff.lower()}'>{diff}</span>"
        f"<span class='chevron'>&#9658;</span></div>"
        f"<div class='card-body collapsed'><div class='tabs'>{tabs}</div>{panels}</div></div>"
    )


def build_guide_card(gf):
    name    = gf.stem
    display = name.replace("_", " ").title()
    cid     = f"guide-{slugify(name)}"
    return (
        f"<div class='card' id='{cid}' data-topic='guides' data-name='{esc(display.lower())}'>"
        f"<div class='card-header' onclick=\"toggleCard('{cid}')\">"
        f"<span class='card-title'>{esc(display)}</span>"
        f"<span class='badge guide'>Guide</span>"
        f"<span class='chevron'>&#9658;</span></div>"
        f"<div class='card-body collapsed'>{md_to_html(read_file(gf))}</div></div>"
    )


sections_html = ""; sidebar_html = ""; total_problems = 0

for topic_slug, topic_name in TOPICS:
    tp = ROOT / topic_slug
    if not tp.exists(): continue
    sid = f"section-{topic_slug}"
    sidebar_html += f"<li><a href='#{sid}'>{esc(topic_name)}</a></li>"
    cards = ""
    if topic_slug == "guides":
        for gf in sorted(tp.glob("*.md")): cards += build_guide_card(gf)
    else:
        for pd in sorted(d for d in tp.iterdir() if d.is_dir()):
            cards += build_problem_card(pd, topic_slug); total_problems += 1
    sections_html += f"<section id='{sid}'><h2 class='section-title'>{esc(topic_name)}</h2>{cards}</section>"

CSS = """
*,*::before,*::after{box-sizing:border-box;margin:0;padding:0}
:root{--bg:#0d1117;--surface:#161b22;--surface2:#21262d;--border:#30363d;
  --accent:#58a6ff;--text:#e6edf3;--muted:#8b949e;
  --easy:#3fb950;--medium:#e3b341;--hard:#f85149;--guide:#58a6ff;--sidebar-w:220px}
body{font-family:'Segoe UI',system-ui,sans-serif;background:var(--bg);color:var(--text);font-size:14px;line-height:1.6}
.layout{display:flex;min-height:100vh}
.sidebar{width:var(--sidebar-w);min-width:var(--sidebar-w);background:var(--surface);
  border-right:1px solid var(--border);padding:1.5rem 0;position:sticky;top:0;height:100vh;overflow-y:auto;flex-shrink:0}
.sidebar h1{font-size:.8rem;color:var(--muted);padding:0 1rem 1rem;text-transform:uppercase;letter-spacing:.08em}
.sidebar ul{list-style:none}
.sidebar li a{display:block;padding:.4rem 1rem;color:var(--muted);text-decoration:none;font-size:.82rem;border-left:2px solid transparent;transition:all .15s}
.sidebar li a:hover,.sidebar li a.active{color:var(--accent);border-left-color:var(--accent);background:var(--surface2)}
.main{flex:1;padding:2rem;max-width:1100px}
.topbar{background:var(--surface);border:1px solid var(--border);border-radius:10px;padding:1.5rem 2rem;margin-bottom:2rem;display:flex;align-items:center;gap:2rem;flex-wrap:wrap}
.topbar-title{font-size:1.4rem;font-weight:700}.topbar-sub{font-size:.82rem;color:var(--muted)}
.stats{display:flex;gap:1.5rem;margin-left:auto}.stat{text-align:center}
.stat-n{font-size:1.5rem;font-weight:700;color:var(--accent)}.stat-l{font-size:.72rem;color:var(--muted);text-transform:uppercase}
.controls{display:flex;gap:.75rem;margin-bottom:1.5rem;flex-wrap:wrap}
.search-box{flex:1;min-width:200px;padding:.55rem .9rem;background:var(--surface);border:1px solid var(--border);border-radius:8px;color:var(--text);font-size:.9rem;outline:none}
.search-box:focus{border-color:var(--accent)}
.filter-btn{padding:.5rem 1rem;border-radius:8px;border:1px solid var(--border);background:var(--surface);color:var(--muted);cursor:pointer;font-size:.82rem;transition:all .15s;font-family:inherit}
.filter-btn:hover,.filter-btn.active{background:var(--accent);color:#000;border-color:var(--accent);font-weight:600}
.section-title{font-size:1.05rem;font-weight:700;color:var(--accent);border-bottom:1px solid var(--border);padding-bottom:.5rem;margin:2rem 0 1rem}
.card{background:var(--surface);border:1px solid var(--border);border-radius:10px;margin-bottom:.75rem;overflow:hidden;transition:border-color .15s}
.card:hover{border-color:#444d56}.card.hidden{display:none}
.card-header{display:flex;align-items:center;gap:.75rem;padding:.9rem 1.2rem;cursor:pointer;user-select:none}
.card-header:hover{background:var(--surface2)}.card-title{font-weight:600;font-size:.95rem;flex:1}
.badge{font-size:.7rem;font-weight:700;padding:.2rem .6rem;border-radius:20px;text-transform:uppercase;letter-spacing:.05em}
.badge.easy{background:rgba(63,185,80,.15);color:var(--easy)}.badge.medium{background:rgba(227,179,65,.15);color:var(--medium)}
.badge.hard{background:rgba(248,81,73,.15);color:var(--hard)}.badge.guide{background:rgba(88,166,255,.15);color:var(--guide)}
.chevron{color:var(--muted);font-size:.85rem;transition:transform .2s}.card.open .chevron{transform:rotate(90deg)}
.card-body{overflow:hidden;border-top:1px solid var(--border)}.card-body.collapsed{display:none}
.tabs{display:flex;border-bottom:1px solid var(--border)}
.tab-btn{padding:.6rem 1.1rem;background:none;border:none;border-bottom:2px solid transparent;color:var(--muted);cursor:pointer;font-size:.82rem;font-family:inherit;transition:all .15s;margin-bottom:-1px}
.tab-btn:hover{color:var(--text)}.tab-btn.active{color:var(--accent);border-bottom-color:var(--accent)}
.tab-panel{display:none;padding:1.2rem}.tab-panel.active{display:block}
pre{background:#0d1117;border:1px solid var(--border);border-radius:8px;padding:1rem;overflow-x:auto;font-size:.82rem;line-height:1.55;font-family:'Cascadia Code','Fira Code',Consolas,monospace}
code{font-family:'Cascadia Code','Fira Code',Consolas,monospace;font-size:.82rem}
p code,li code{background:var(--surface2);border:1px solid var(--border);padding:.1em .35em;border-radius:4px;color:#e3b341}
.kw{color:#ff7b72}.cm{color:#8b949e;font-style:italic}.st{color:#a5d6ff}.nm{color:#79c0ff}
.tab-panel h2{font-size:1.05rem;color:var(--accent);border-bottom:1px solid var(--border);padding-bottom:.3rem;margin:1.2rem 0 .4rem}
.tab-panel h3,.tab-panel h4{color:var(--text);margin:1rem 0 .3rem;font-size:.95rem}
.tab-panel p{margin:.5rem 0;color:var(--muted);font-size:.88rem}
.tab-panel li{margin:.25rem 0 .25rem 1.5rem;color:var(--muted);font-size:.88rem}
.tab-panel hr{border:none;border-top:1px solid var(--border);margin:1rem 0}
.tab-panel table{border-collapse:collapse;width:100%;margin:.75rem 0;font-size:.82rem}
.tab-panel th,.tab-panel td{border:1px solid var(--border);padding:.4rem .8rem;text-align:left}
.tab-panel th{background:var(--surface2);color:var(--text);font-weight:600}.tab-panel td{color:var(--muted)}
.tab-panel a{color:var(--accent)}.tab-panel strong{color:var(--text)}
.lc-link{font-size:.72rem;font-weight:700;padding:.18rem .55rem;border-radius:6px;background:rgba(255,161,22,.12);color:#ffa116;text-decoration:none;border:1px solid rgba(255,161,22,.25);transition:all .15s;white-space:nowrap}.lc-link:hover{background:rgba(255,161,22,.25);color:#ffa116}
::-webkit-scrollbar{width:6px;height:6px}::-webkit-scrollbar-track{background:transparent}
::-webkit-scrollbar-thumb{background:var(--border);border-radius:3px}
@media(max-width:768px){.sidebar{display:none}.main{padding:1rem}}
"""

JS = """
function toggleCard(id){var c=document.getElementById(id),b=c.querySelector('.card-body');c.classList.toggle('open');b.classList.toggle('collapsed');}
function expandAll(){document.querySelectorAll('.card:not(.hidden)').forEach(function(c){c.classList.add('open');c.querySelector('.card-body').classList.remove('collapsed');});}
function collapseAll(){document.querySelectorAll('.card').forEach(function(c){c.classList.remove('open');c.querySelector('.card-body').classList.add('collapsed');});}
function switchTab(e,pid){var card=e.target.closest('.card');card.querySelectorAll('.tab-btn').forEach(function(b){b.classList.remove('active');});card.querySelectorAll('.tab-panel').forEach(function(p){p.classList.remove('active');});e.target.classList.add('active');document.getElementById(pid).classList.add('active');}
var activeDiff='all';
function filterDiff(diff,btn){activeDiff=diff;document.querySelectorAll('.filter-btn').forEach(function(b){b.classList.remove('active');});btn.classList.add('active');filterCards();}
function filterCards(){var q=document.getElementById('search').value.toLowerCase();document.querySelectorAll('.card').forEach(function(card){var name=(card.dataset.name||'').toLowerCase();var topic=(card.dataset.topic||'').toLowerCase();var badge=card.querySelector('.badge');var diff=badge?badge.className.replace('badge ','').toLowerCase():'';var ok=(!q||name.includes(q)||topic.includes(q))&&(activeDiff==='all'||diff===activeDiff);card.classList.toggle('hidden',!ok);});}
var secs=document.querySelectorAll('section[id]'),navs=document.querySelectorAll('.sidebar li a');
if('IntersectionObserver' in window){var obs=new IntersectionObserver(function(entries){entries.forEach(function(e){if(e.isIntersecting)navs.forEach(function(a){a.classList.toggle('active',a.getAttribute('href')==='#'+e.target.id);});});},{rootMargin:'-20% 0px -70% 0px'});secs.forEach(function(s){obs.observe(s);});}
"""

page = (
    "<!DOCTYPE html><html lang='en'><head>"
    "<meta charset='UTF-8'><meta name='viewport' content='width=device-width,initial-scale=1.0'>"
    "<title>DSA Revision &mdash; Staff/Principal Engineer Prep</title>"
    f"<style>{CSS}</style></head><body>"
    "<div class='layout'>"
    f"<nav class='sidebar'><h1>DSA Topics</h1><ul>{sidebar_html}</ul></nav>"
    "<main class='main'>"
    "<div class='topbar'><div><div class='topbar-title'>DSA Revision</div>"
    "<div class='topbar-sub'>Staff / Principal Engineer Preparation</div></div>"
    f"<div class='stats'><div class='stat'><div class='stat-n'>{total_problems}</div>"
    "<div class='stat-l'>Problems</div></div>"
    "<div class='stat'><div class='stat-n'>15</div><div class='stat-l'>Guides</div></div></div></div>"
    "<div class='controls'>"
    "<input class='search-box' type='text' id='search' placeholder='Search problems, topics, patterns...' oninput='filterCards()'>"
    "<button class='filter-btn active' onclick=\"filterDiff('all',this)\">All</button>"
    "<button class='filter-btn' onclick=\"filterDiff('easy',this)\">Easy</button>"
    "<button class='filter-btn' onclick=\"filterDiff('medium',this)\">Medium</button>"
    "<button class='filter-btn' onclick=\"filterDiff('hard',this)\">Hard</button>"
    "<button class='filter-btn' onclick=\"filterDiff('guide',this)\">Guides</button>"
    "<button class='filter-btn' onclick='expandAll()'>Expand All</button>"
    "<button class='filter-btn' onclick='collapseAll()'>Collapse All</button>"
    f"</div>{sections_html}</main></div>"
    f"<script>{JS}</script></body></html>"
)

(SITE / "index.html").write_text(page, encoding="utf-8")
print(f"Generated site/index.html  ({len(page):,} bytes)  problems={total_problems}")
