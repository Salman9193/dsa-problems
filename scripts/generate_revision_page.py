#!/usr/bin/env python3
"""Generate DSA revision page — single HTML file."""
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

TOPIC_META = {
    "arrays":              ("🧩", "Foundation"),
    "strings":             ("🔤", "Foundation"),
    "linked-list":         ("🔗", "Foundation"),
    "binary-search":       ("🔍", "Foundation"),
    "stacks":              ("📚", "Foundation"),
    "dynamic-programming": ("💡", "Patterns"),
    "trees":               ("🌳", "Patterns"),
    "graphs":              ("🕸️",  "Patterns"),
    "design":              ("⚙️",  "Advanced"),
    "bit-manipulation":    ("⚡", "Advanced"),
    "guides":              ("📖", "Guides"),
}

LC = {
    "two-sum-ii":(167,"two-sum-ii-input-array-is-sorted"),
    "3sum":(15,"3sum"),
    "container-with-most-water":(11,"container-with-most-water"),
    "trapping-rain-water":(42,"trapping-rain-water"),
    "product-of-array-except-self":(238,"product-of-array-except-self"),
    "maximum-of-absolute-value-expression":(1131,"maximum-of-absolute-value-expression"),
    "shortest-common-supersequence":(1092,"shortest-common-supersequence"),
    "palindrome-linked-list":(234,"palindrome-linked-list"),
    "sort-characters-by-frequency":(451,"sort-characters-by-frequency"),
    "flatten-a-multilevel-doubly-linked-list":(430,"flatten-a-multilevel-doubly-linked-list"),
    "reverse-bits":(190,"reverse-bits"),
    "max-points-on-a-line":(149,"max-points-on-a-line"),
    "largest-divisible-subset":(368,"largest-divisible-subset"),
    "combination-sum-ii":(40,"combination-sum-ii"),
    "combination-sum-iv":(377,"combination-sum-iv"),
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
    "palindromic-substrings":(647,"palindromic-substrings"),
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
    "serialize-deserialize-binary-tree":(297,"serialize-and-deserialize-binary-tree"),
    "range-sum-query-mutable":(307,"range-sum-query-mutable"),
    "merge-k-sorted-lists":(23,"merge-k-sorted-lists"),
    "word-search-ii":(212,"word-search-ii"),
    "find-duplicate-subtrees":(652,"find-duplicate-subtrees"),
}

ROOT = Path(".")
SITE = Path("site")
SITE.mkdir(exist_ok=True)

JAVA_KW = {
    "public","private","protected","static","final","class","interface",
    "void","int","long","boolean","char","String","return","new","if","else",
    "for","while","break","continue","null","true","false","this","import",
    "List","Map","Set","Queue","Deque","Arrays","Collections","ArrayList",
    "HashMap","HashSet","LinkedList","ArrayDeque","PriorityQueue","TreeMap",
    "TreeSet","Integer","Math","throws","throw","try","catch","finally",
    "extends","implements",
}

def slugify(s):
    return re.sub(r"[^a-z0-9-]", "-", s.lower()).strip("-")

def read_file(path):
    try:
        return path.read_text(encoding="utf-8")
    except Exception:
        return ""

def esc(s):
    return html.escape(str(s), quote=True)

def highlight_code_token(text):
    out = []
    i = 0
    n = len(text)
    while i < n:
        c = text[i]
        if c.isalpha() or c == '_':
            j = i
            while j < n and (text[j].isalnum() or text[j] == '_'):
                j += 1
            word = text[i:j]
            if word in JAVA_KW:
                out.append("<span class='kw'>" + word + "</span>")
            else:
                out.append(html.escape(word, quote=False))
            i = j
        elif c.isdigit():
            j = i
            while j < n and (text[j].isdigit() or text[j] in '.xXaAbBcCdDeEfFLl_'):
                j += 1
            out.append("<span class='nm'>" + html.escape(text[i:j], quote=False) + "</span>")
            i = j
        else:
            out.append(html.escape(c, quote=False))
            i += 1
    return "".join(out)

def highlight_java(raw_code):
    out = []
    i = 0
    s = raw_code
    n = len(s)
    while i < n:
        if s[i:i+2] == "/*":
            end = s.find("*/", i+2)
            end = end + 2 if end != -1 else n
            out.append("<span class='cm'>" + html.escape(s[i:end], quote=False) + "</span>")
            i = end
        elif s[i:i+2] == "//":
            end = s.find("\n", i)
            end = end if end != -1 else n
            out.append("<span class='cm'>" + html.escape(s[i:end], quote=False) + "</span>")
            i = end
        elif s[i] == '"':
            j = i + 1
            while j < n:
                if s[j] == '\\': j += 2
                elif s[j] == '"': j += 1; break
                else: j += 1
            out.append("<span class='st'>" + html.escape(s[i:j], quote=False) + "</span>")
            i = j
        elif s[i] == "'":
            j = i + 1
            while j < n:
                if s[j] == '\\': j += 2
                elif s[j] == "'": j += 1; break
                else: j += 1
            out.append("<span class='st'>" + html.escape(s[i:j], quote=False) + "</span>")
            i = j
        else:
            j = i
            while j < n and s[j:j+2] not in ("//","/*") and s[j] not in ('"',"'"):
                j += 1
            if j > i:
                out.append(highlight_code_token(s[i:j]))
            i = j
    return "".join(out)

def inline_md(text):
    text = re.sub(r"`([^`]+)`", lambda m: "<code>" + esc(m.group(1)) + "</code>", text)
    text = re.sub(r"\*\*([^*]+)\*\*", r"<strong>\1</strong>", text)
    text = re.sub(r"\*([^*]+)\*", r"<em>\1</em>", text)
    text = re.sub(r"\[([^\]]+)\]\(([^)]+)\)",
        lambda m: '<a href="' + esc(m.group(2)) + '" target="_blank">' + esc(m.group(1)) + '</a>', text)
    # auto-link bare URLs (not already inside an href="..."); keep balanced parens, strip trailing punctuation
    def _autolink(m):
        url = m.group(1); trail = ""
        while url:
            c = url[-1]
            if c in ".,;:":
                trail = c + trail; url = url[:-1]
            elif c == ")" and url.count(")") > url.count("("):
                trail = c + trail; url = url[:-1]
            else:
                break
        return ('<a href="' + url + '" target="_blank" rel="noopener">' + url + "</a>" + trail)
    text = re.sub(r'(?<![">=])(https?://[^\s<>"\]]+)', _autolink, text)
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
                out.append("<pre><code>" + body + "</code></pre>")
                code_buf = []; code_lang = ""; in_code = False; in_table = False; had_th = False
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
            out.append("<tr>" + "".join("<" + tag + ">" + inline_md(c) + "</" + tag + ">" for c in cells) + "</tr>")
            continue
        else:
            if in_table: out.append("</table>"); in_table = False; had_th = False
        m = re.match(r"^(#{1,4})\s+(.*)", line)
        if m:
            lvl = min(len(m.group(1)) + 1, 6)
            out.append("<h" + str(lvl) + ">" + inline_md(m.group(2)) + "</h" + str(lvl) + ">"); continue
        if re.match(r"^-{3,}$", line.strip()):
            out.append("<hr>"); continue
        m = re.match(r"^\s*[-*]\s+(.*)", line)
        if m: out.append("<li>" + inline_md(m.group(1)) + "</li>"); continue
        m = re.match(r"^\s*\d+\.\s+(.*)", line)
        if m: out.append("<li>" + inline_md(m.group(1)) + "</li>"); continue
        if not line.strip(): out.append("<br>"); continue
        out.append("<p>" + inline_md(line) + "</p>")
    if in_table: out.append("</table>")
    return "\n".join(out)

def build_problem_card(prob_dir, topic_slug):
    name = prob_dir.name
    display = name.replace("-", " ").title()
    notes = read_file(prob_dir / "NOTES.md")
    diff = "Medium"
    if "| Easy" in notes or "Easy |" in notes: diff = "Easy"
    elif "| Hard" in notes or "Hard |" in notes: diff = "Hard"
    lc_badge = ""
    if name in LC:
        lc_num, lc_slug = LC[name]
        lc_badge = ("<a class='lc-link' href='https://leetcode.com/problems/" + lc_slug + "/' "
                    "target='_blank' onclick='event.stopPropagation()'>#" + str(lc_num) + "</a>")
    hash_id = topic_slug + "/" + name
    return (
        "<div class='card' data-topic='" + topic_slug + "' data-name='" + esc(display.lower()) + "' "
        "data-diff='" + diff.lower() + "' onclick=\"navTo('" + hash_id + "')\">"        "<span class='card-title'>" + esc(display) + "</span>"
        + lc_badge +
        "<span class='badge " + diff.lower() + "'>" + diff + "</span>"
        "<span class='card-arrow'>&#8250;</span>"
        "</div>"
    )


def build_problem_page(prob_dir, topic_slug):
    name = prob_dir.name
    display = name.replace("-", " ").title()
    pid = "prob-" + topic_slug + "-" + name
    sol   = read_file(prob_dir / "Solution.java")
    notes = read_file(prob_dir / "NOTES.md")
    uses  = read_file(prob_dir / "USE_CASES.md")
    research = read_file(prob_dir / "RESEARCH.md")
    diff = "Medium"
    if "| Easy" in notes or "Easy |" in notes: diff = "Easy"
    elif "| Hard" in notes or "Hard |" in notes: diff = "Hard"
    lc_badge = ""
    if name in LC:
        lc_num, lc_slug = LC[name]
        lc_badge = ("<a class='lc-link' href='https://leetcode.com/problems/" + lc_slug + "/' "
                    "target='_blank'>#" + str(lc_num) + "</a>")
    tabs = panels = ""
    if sol:
        tabs   += "<button class='tab-btn active' onclick=\"switchTab(event,'" + pid + "-sol')\">Solution.java</button>"
        panels += "<div id='" + pid + "-sol' class='tab-panel active'><pre><code>" + highlight_java(sol) + "</code></pre></div>"
    if notes:
        tabs   += "<button class='tab-btn' onclick=\"switchTab(event,'" + pid + "-notes')\">Notes</button>"
        panels += "<div id='" + pid + "-notes' class='tab-panel'>" + md_to_html(notes) + "</div>"
    if uses:
        tabs   += "<button class='tab-btn' onclick=\"switchTab(event,'" + pid + "-uses')\">Use Cases</button>"
        panels += "<div id='" + pid + "-uses' class='tab-panel'>" + md_to_html(uses) + "</div>"
    if research:
        tabs   += "<button class='tab-btn' onclick=\"switchTab(event,'" + pid + "-research')\">Research &amp; Foundations</button>"
        panels += "<div id='" + pid + "-research' class='tab-panel'>" + md_to_html(research) + "</div>"
    topic_display = dict(TOPICS).get(topic_slug, topic_slug.replace("-"," ").title())
    return (
        "<div id='" + pid + "' class='prob-page'>"
        "<div class='prob-header'>"
        "<button class='back-btn' onclick=\"navTo('section-" + topic_slug + "')\">&#8592; " + esc(topic_display) + "</button>"
        "<div class='prob-title-row'>"
        "<h2 class='prob-title'>" + esc(display) + "</h2>"
        + lc_badge +
        "<span class='badge " + diff.lower() + "'>" + diff + "</span>"
        "</div></div>"
        "<div class='tabs'>" + tabs + "</div>"
        + panels +
        "</div>"
    )

def build_guide_card(gf):
    name = gf.stem
    display = name.replace("_", " ").title()
    hash_id = "guides/" + name
    return (
        "<div class='card' data-topic='guides' data-name='" + esc(display.lower()) + "' "
        "data-diff='guide' onclick=\"navTo('" + hash_id + "')\">"        "<span class='card-title'>" + esc(display) + "</span>"
        "<span class='badge guide'>Guide</span>"
        "<span class='card-arrow'>&#8250;</span>"
        "</div>"
    )


def build_guide_page(gf):
    name = gf.stem
    display = name.replace("_", " ").title()
    gid = "guide-" + slugify(name)
    content = md_to_html(read_file(gf))
    return (
        "<div id='" + gid + "' class='prob-page'>"
        "<div class='prob-header'>"
        "<button class='back-btn' onclick=\"navTo('section-guides')\">&#8592; Concept Guides</button>"
        "<div class='prob-title-row'>"
        "<h2 class='prob-title'>" + esc(display) + "</h2>"
        "<span class='badge guide'>Guide</span>"
        "</div></div>"
        "<div class='guide-body'>" + content + "</div>"
        "</div>"
    )



def build_roadmap_section():
    nodes = [
        ("foundation","🧱","Arrays & Hashing","easy","6 problems","Week 1",
         ["Two-pointer: sorted arrays, opposite ends","Prefix sums for O(1) range queries","HashMap O(1): group anagrams, top-K","Progression: Two Sum II to 3Sum to Container"],
         ["Two Sum II","3Sum","Product Except Self","Group Anagrams","Top K Frequent","Longest Consecutive"]),
        ("foundation","🪟","Sliding Window","easy","4 problems","Week 1",
         ["Fixed window: running sum, slide right each step","Variable window: shrink left when constraint violated","Never shrink unnecessarily: Longest Repeating trick","Progression: Longest Substring to Min Window"],
         ["Longest Substring","Longest Repeating","Min Window Substring","Find All Anagrams"]),
        ("foundation","🔗","Linked Lists","easy","3 problems","Week 2",
         ["Fast/slow pointer for cycle detection and midpoint","Dummy head eliminates nil-check edge cases","In-place reversal: track prev, curr, next"],
         ["Reverse Linked List","Linked List Cycle","Linked List Random Node"]),
        ("foundation","📚","Stack & Queue","easy","4 problems","Week 2",
         ["Recognise LIFO: matching brackets, expression eval","Monotonic DECREASING stack: next greater element","Monotonic INCREASING stack: next smaller (histogram)","Deque for sliding window max: O(n) not O(nk)"],
         ["Valid Parentheses","Daily Temperatures","Sliding Window Max","Largest Rectangle"]),
        ("core","🔍","Binary Search","medium","5 problems","Week 3",
         ["Identify: search space, monotone predicate, invariant","lo=0,hi=n-1 vs lo=0,hi=n: know which for each","Binary search ON ANSWER: search the answer space","Rotated array: identify sorted half, decide side"],
         ["Search Rotated Array","Find in Mountain Array","Sqrt(x)","Online Election","Kth Largest"]),
        ("core","🌳","Trees & BFS/DFS","medium","8 problems","Week 3-4",
         ["DFS: preorder, inorder (BST sorted), postorder","Two-return-value: through vs upward per node","BFS: process entire level before advancing","Range-passing for BST validation"],
         ["Max Path Sum","Validate BST","LCA","BST Iterator","Populating Pointers","LCA Deepest Leaves","All Full Binary Trees","Delete Return Forest"]),
        ("core","💡","1D Dynamic Programming","medium","6 problems","Week 4",
         ["Confirm: optimal substructure + overlapping subproblems","Draw the recurrence before coding","Space optimise: full array vs just prev values","Take-or-skip: House Robber to Robber II to Robber III"],
         ["Climbing Stairs","Coin Change","House Robber","Word Break","Jump Game","Partition Equal Subset"]),
        ("core","📐","Intervals","medium","4 problems","Week 5",
         ["Always sort by start time first","Overlap: curr.start <= last.end (touching counts)","Meeting Rooms II: min-heap of end times","Non-overlapping: sort by END, keep earliest finishing"],
         ["Merge Intervals","Meeting Rooms II","Insert Interval","Non-overlapping Intervals"]),
        ("core","🕸️","Graph Traversal","medium","12 problems","Week 5-6",
         ["BFS = shortest path; DFS = connectivity, cycles","Mark visited ON ENQUEUE not on dequeue","Multi-source BFS: add all sources before starting","Topological sort: Kahn BFS or DFS post-order"],
         ["Number of Islands","Rotting Oranges","01 Matrix","Word Ladder","Pacific Atlantic","Course Schedule","Course Schedule II","Clone Graph"]),
        ("advanced","🔀","Backtracking","hard","5 problems","Week 7",
         ["Template: choose, explore, un-choose (the backtrack step)","Subsets: start index moves forward, no revisit","Permutations: visited[] lets any unused element be chosen","Pruning: sort candidates, break early on constraint","Grid: in-place mark, restore exactly on backtrack"],
         ["Subsets","Permutations","Combination Sum","Word Search","N-Queens"]),
        ("advanced","📊","Heaps","hard","4 problems","Week 7",
         ["Min-heap of size k: top-k, works on streaming","Two-heap design: lo=max-heap, hi=min-heap, size invariant","Quickselect: O(n) avg, randomise pivot for safety","Merge k sorted: heap on (val, list_idx, elem_idx)"],
         ["Kth Largest Element","Find Median Data Stream","Merge K Sorted Lists","Task Scheduler"]),
        ("advanced","🗺️","Weighted Graphs","hard","8 problems","Week 8",
         ["Dijkstra: min-heap, relax edges, O((V+E) log V)","Bellman-Ford: relax all edges V-1 times","Union-Find: path compression + rank, near O(1)","MST: Kruskal (sort + DSU) or Prim (min-heap)"],
         ["Network Delay","Cheapest Flights","Path Min Effort","Min Cost Connect","Redundant Connection","Accounts Merge","Swim Rising Water","Critical Connections"]),
        ("advanced","🎯","2D Dynamic Programming","hard","5 problems","Week 8-9",
         ["State: dp[i][j] uses first i of A and first j of B","LCS: match gives dp[i-1][j-1]+1, else max(skip)","Unique Paths: dp[r][c] = from above + from left","Space optimise: single row, left-to-right"],
         ["Unique Paths","LIS","Cherry Pickup II","Edit Distance","LCS"]),
        ("staff","🏗️","Interval DP","hard","3 problems","Week 10",
         ["Template: for length, for left, for k split point","Burst Balloons: choose LAST to burst, fixed neighbours","Matrix chain: dp[i][j] min cost for matrices i..j","Merge Stones: only valid if (n-1) mod (k-1) == 0"],
         ["Burst Balloons","Minimum Cost Merge Stones","Matrix Chain"]),
        ("staff","🌲","Trie & Advanced Strings","hard","3 problems","Week 10",
         ["Trie: O(L) prefix queries beats HashMap O(WxL)","Word Search II: Trie + DFS traversal to prune","Binary Trie: XOR maximisation, pick opposite bit","Mention Aho-Corasick and suffix arrays at Staff+"],
         ["Implement Trie","Word Search II","Maximum XOR Two Numbers"]),
        ("staff","⚙️","Design Problems","hard","3 problems","Week 11",
         ["LRU Cache: DLL + HashMap, O(1) get and put","Median Finder: route-through-lo, 3-line balance","Exam Room: TreeSet of seats, custom gap comparator","Connect tradeoffs to scale: 1M ops/sec implications"],
         ["LRU Cache","Find Median from Data Stream","Exam Room"]),
        ("staff","🧠","Complexity & Proofs","hard","Guides","Week 11-12",
         ["Exchange argument: prove greedy works for scheduling","Know P/NP: subset sum NP-hard, coin change poly","Amortised: DSU path compression, dynamic array","Reduction: map unknown to known, prove equivalence"],
         ["Greedy vs DP Guide","Complexity Theory Guide","Graph Cycle Guide","Knapsack Variants Guide"]),
    ]

    phase_labels = {
        "foundation": "Phase 1 — Foundation (Weeks 1-2)",
        "core":       "Phase 2 — Core Patterns (Weeks 3-6)",
        "advanced":   "Phase 3 — Advanced (Weeks 7-9)",
        "staff":      "Phase 4 — Staff Level (Weeks 10-12)",
    }
    phase_order = ["foundation", "core", "advanced", "staff"]

    cards_html = ""
    seen_phases = set()
    for (phase, icon, title, diff, count, weeks, steps, probs) in nodes:
        if phase not in seen_phases:
            if seen_phases:
                cards_html += "<div class='rm-arrow'>&#8595;</div>"
            cards_html += "<div class='rm-phase-lbl'>" + phase_labels[phase] + "</div>"
            seen_phases.add(phase)

        diff_cls = "rm-" + diff
        steps_html = "".join(
            "<li><span class='rm-sn'>" + str(i+1) + "</span>" + s + "</li>"
            for i, s in enumerate(steps)
        )
        probs_html = "".join("<span class='rm-chip'>" + p + "</span>" for p in probs)
        cid = "rm-" + slugify(title)

        cards_html += (
            "<div class='rm-node' id='" + cid + "' onclick=\"toggleRM('" + cid + "')\">"
            "<div class='rm-top'>"
            "<span class='rm-icon'>" + icon + "</span>"
            "<span class='rm-title'>" + title + "</span>"
            "<span class='rm-badge " + diff_cls + "'>" + count + "</span>"
            "</div>"
            "<div class='rm-body'>"
            "<span class='rm-time'>&#9200; " + weeks + "</span>"
            "<h4>How to prepare</h4><ul class='rm-steps'>" + steps_html + "</ul>"
            "<h4 style='margin-top:.6rem'>Key problems</h4>"
            "<div class='rm-chips'>" + probs_html + "</div>"
            "</div></div>"
        )

    pills = "".join(
        "<button class='rm-pill' onclick=\"filterRM('" + p + "',this)\">" +
        p.title() + "</button>"
        for p in ["all", "foundation", "core", "advanced", "staff"]
    )

    return (
        "<section id='section-roadmap' class='topic-section'>"
        "<h2 class='section-title'><span>🗺️</span>Preparation Roadmap"
        "<span class='section-title-count'>17 topics</span></h2>"
        "<div class='rm-wrap'>"
        "<p class='rm-sub'>Click any topic to expand preparation steps. Track progress as you explore.</p>"
        "<div class='rp-labels'><span>Topics explored</span><strong id='rm-pct'>0 / 17</strong></div>"
        "<div class='rp-bar-bg'><div class='rp-bar' id='rm-bar' style='width:0%'></div></div>"
        "<div class='rm-pills'>" + pills + "</div>"
        "<div id='rm-grid'>" + cards_html + "</div>"
        "</div></section>"
    )

# ── Build sidebar + sections ──────────────────────────────────────────────────
emitted_groups = set()
sidebar_html = ""
sections_html = ""
total_problems = 0
all_problem_pages = ""

# Roadmap nav entry
sidebar_html += "<div class='nav-group'>Guides</div>"
sidebar_html += "<li><a href='#section-roadmap' onclick=\"showSection('section-roadmap');return false;\"><span class='nav-icon'>🗺&#xFE0F;</span>Preparation Roadmap</a></li>"
emitted_groups.add("Guides")

for topic_slug, topic_name in TOPICS:
    tp = ROOT / topic_slug
    if not tp.exists(): continue
    sid = "section-" + topic_slug
    meta = TOPIC_META.get(topic_slug, ("📁", "Foundation"))
    icon, group = meta
    if group not in emitted_groups:
        sidebar_html += "<div class='nav-group'>" + group + "</div>"
        emitted_groups.add(group)
    cnt = len([d for d in tp.iterdir() if d.is_dir()]) if topic_slug != "guides" else len(list(tp.glob("*.md")))
    sidebar_html += (
        "<li><a href='#" + sid + "' onclick=\"showSection('" + sid + "');return false;\">"
        "<span class='nav-icon'>" + icon + "</span>" + esc(topic_name) +
        "<span class='nav-count'>" + str(cnt) + "</span></a></li>"
    )
    cards = ""; pages = ""
    if topic_slug == "guides":
        for gf in sorted(tp.glob("*.md")):
            cards += build_guide_card(gf)
            pages += build_guide_page(gf)
    else:
        for pd in sorted(d for d in tp.iterdir() if d.is_dir()):
            cards += build_problem_card(pd, topic_slug)
            pages += build_problem_page(pd, topic_slug)
            total_problems += 1
    all_problem_pages += pages
    si = TOPIC_META.get(topic_slug, ("📁",""))[0]
    pc = len([d for d in tp.iterdir() if d.is_dir()]) if topic_slug != "guides" else len(list(tp.glob("*.md")))
    unit = "problems" if topic_slug != "guides" else "guides"
    sections_html += (
        "<section id='" + sid + "' class='topic-section'>"
        "<h2 class='section-title'><span>" + si + "</span>" + esc(topic_name) +
        "<span class='section-title-count'>" + str(pc) + " " + unit + "</span></h2>"
        + cards + "</section>"
    )

roadmap_html = build_roadmap_section()

# ── CSS ───────────────────────────────────────────────────────────────────────
CSS = """
*,*::before,*::after{box-sizing:border-box;margin:0;padding:0}
:root{
  --bg:#f6f8fa;--surface:#ffffff;--surface2:#f0f3f6;--border:#d0d7de;
  --accent:#0969da;--text:#1f2328;--muted:#656d76;
  --easy:#1a7f37;--medium:#9a6700;--hard:#d1242f;--guide:#0969da;
  --sidebar-w:240px
}
body{font-family:'Segoe UI',system-ui,sans-serif;background:var(--bg);color:var(--text);font-size:14px;line-height:1.6}
.layout{display:flex;min-height:100vh}

/* Sidebar */
.sidebar{width:var(--sidebar-w);min-width:var(--sidebar-w);background:var(--surface);
  border-right:1px solid var(--border);padding:1rem 0;position:sticky;top:0;
  height:100vh;overflow-y:auto;flex-shrink:0}
.sidebar-brand{font-size:.72rem;font-weight:700;color:var(--muted);
  padding:0 1rem .75rem;text-transform:uppercase;letter-spacing:.1em}
.nav-group{font-size:.62rem;font-weight:700;color:var(--muted);
  padding:.65rem 1rem .2rem;text-transform:uppercase;letter-spacing:.1em;
  border-top:1px solid var(--border);margin-top:.25rem}
.sidebar ul{list-style:none}
.sidebar li a{display:flex;align-items:center;gap:.5rem;padding:.38rem .75rem .38rem 1rem;
  color:var(--muted);text-decoration:none;font-size:.82rem;
  border-left:3px solid transparent;transition:all .12s;
  border-radius:0 6px 6px 0;margin-right:.5rem}
.sidebar li a:hover{color:var(--text);background:var(--surface2);border-left-color:var(--border)}
.sidebar li a.active{color:var(--accent);border-left-color:var(--accent);
  background:#e8f0fb;font-weight:600}
.nav-icon{font-size:.85rem;flex-shrink:0;opacity:.75}
.sidebar li a.active .nav-icon{opacity:1}
.nav-count{margin-left:auto;font-size:.65rem;font-weight:600;padding:.1rem .35rem;
  border-radius:8px;background:var(--surface2);color:var(--muted)}
.sidebar li a.active .nav-count{background:#d0e4fb;color:var(--accent)}

/* Main */
.main{flex:1;padding:2rem;min-width:0}

/* Section show/hide — THE KEY RULE */
.topic-section{display:none}
.topic-section.active{display:block}

/* Section header */
.section-title{font-size:1.05rem;font-weight:700;color:var(--text);
  border-bottom:2px solid var(--border);padding-bottom:.75rem;margin:0 0 1.5rem;
  display:flex;align-items:center;gap:.5rem}
.section-title-count{margin-left:auto;font-size:.72rem;padding:.25rem .7rem;
  border-radius:20px;background:var(--surface2);color:var(--muted);
  border:1px solid var(--border);font-weight:500}

/* Topbar */
.topbar{background:var(--surface);border:1px solid var(--border);border-radius:10px;
  padding:1.25rem 1.5rem;margin-bottom:1.5rem;display:flex;align-items:center;gap:2rem;flex-wrap:wrap}
.topbar-title{font-size:1.3rem;font-weight:700}
.topbar-sub{font-size:.8rem;color:var(--muted)}
.stats{display:flex;gap:1.5rem;margin-left:auto}
.stat{text-align:center}
.stat-n{font-size:1.4rem;font-weight:700;color:var(--accent)}
.stat-l{font-size:.7rem;color:var(--muted);text-transform:uppercase}

/* Controls */
.controls{display:flex;gap:.6rem;margin-bottom:1.5rem;flex-wrap:wrap}
.search-box{flex:1;min-width:180px;padding:.5rem .85rem;background:var(--surface);
  border:1px solid var(--border);border-radius:8px;color:var(--text);font-size:.88rem;outline:none}
.search-box:focus{border-color:var(--accent)}
.filter-btn{padding:.45rem .9rem;border-radius:8px;border:1px solid var(--border);
  background:var(--surface);color:var(--muted);cursor:pointer;font-size:.8rem;
  transition:all .15s;font-family:inherit}
.filter-btn:hover,.filter-btn.active{background:var(--accent);color:#fff;
  border-color:var(--accent);font-weight:600}

/* Cards */
.card{background:var(--surface);border:1px solid var(--border);border-radius:10px;
  margin-bottom:.5rem;display:flex;align-items:center;gap:.75rem;
  padding:.75rem 1.1rem;cursor:pointer;transition:all .15s}
.card:hover{border-color:var(--accent);background:var(--surface2);transform:translateX(2px)}
.card.hidden{display:none}
.card-title{font-weight:600;font-size:.88rem;flex:1}
.card-arrow{color:var(--muted);font-size:1.2rem;margin-left:auto;flex-shrink:0}
.prob-page{display:none}.prob-page.active{display:block}
#prob-pages{display:none}
.prob-header{margin-bottom:1.25rem}
.back-btn{display:inline-flex;align-items:center;gap:.4rem;padding:.35rem .75rem;
  background:var(--surface2);border:1px solid var(--border);border-radius:8px;
  color:var(--muted);cursor:pointer;font-size:.8rem;font-family:inherit;transition:all .15s;margin-bottom:.85rem}
.back-btn:hover{border-color:var(--accent);color:var(--accent)}
.prob-title-row{display:flex;align-items:center;gap:.65rem;flex-wrap:wrap;margin-bottom:1rem}
.prob-title{font-size:1.1rem;font-weight:700;color:var(--text)}
.guide-body{padding:.25rem 0}
.lc-link{font-size:.7rem;font-weight:700;padding:.15rem .5rem;border-radius:6px;
  background:rgba(255,161,22,.1);color:#b45309;text-decoration:none;
  border:1px solid rgba(255,161,22,.3);transition:all .15s}
.lc-link:hover{background:rgba(255,161,22,.2)}
.badge{font-size:.68rem;font-weight:700;padding:.18rem .55rem;border-radius:20px;text-transform:uppercase}
.badge.easy{background:rgba(26,127,55,.12);color:var(--easy)}
.badge.medium{background:rgba(154,103,0,.12);color:var(--medium)}
.badge.hard{background:rgba(209,36,47,.12);color:var(--hard)}
.badge.guide{background:rgba(9,105,218,.12);color:var(--guide)}
/* card inline-expand CSS removed — problems now open as full pages */
.tabs{display:flex;border-bottom:1px solid var(--border)}
.tab-btn{padding:.55rem 1rem;background:none;border:none;
  border-bottom:2px solid transparent;color:var(--muted);cursor:pointer;
  font-size:.8rem;font-family:inherit;transition:all .15s;margin-bottom:-1px}
.tab-btn:hover{color:var(--text)}
.tab-btn.active{color:var(--accent);border-bottom-color:var(--accent)}
.tab-panel{display:none;padding:1.1rem}
.tab-panel.active{display:block}
pre{background:#f6f8fa;border:1px solid var(--border);border-radius:8px;padding:1rem;
  overflow-x:auto;font-size:.8rem;line-height:1.55;color:#1f2328;
  font-family:'Cascadia Code','Fira Code',Consolas,monospace}
code{font-family:'Cascadia Code','Fira Code',Consolas,monospace;font-size:.8rem}
p code,li code{background:var(--surface2);border:1px solid var(--border);
  padding:.1em .35em;border-radius:4px;color:#b45309}
.kw{color:#cf222e}.cm{color:#6e7781;font-style:italic}.st{color:#0a3069}.nm{color:#0550ae}
.tab-panel h2{font-size:1rem;color:var(--accent);border-bottom:1px solid var(--border);
  padding-bottom:.3rem;margin:1.1rem 0 .35rem}
.tab-panel h3,.tab-panel h4{color:var(--text);margin:.9rem 0 .3rem;font-size:.9rem}
.tab-panel p{margin:.45rem 0;color:var(--muted);font-size:.86rem}
.tab-panel li{margin:.22rem 0 .22rem 1.4rem;color:var(--muted);font-size:.86rem}
.tab-panel hr{border:none;border-top:1px solid var(--border);margin:.9rem 0}
.tab-panel table{border-collapse:collapse;width:100%;margin:.65rem 0;font-size:.8rem}
.tab-panel th,.tab-panel td{border:1px solid var(--border);padding:.35rem .75rem;text-align:left}
.tab-panel th{background:var(--surface2);color:var(--text);font-weight:600}
.tab-panel td{color:var(--muted)}
.tab-panel a{color:var(--accent)}
.tab-panel strong{color:var(--text)}

/* Roadmap */
.rm-wrap{background:var(--surface);border:1px solid var(--border);border-radius:10px;padding:1.25rem}
.rm-sub{font-size:.82rem;color:var(--muted);margin-bottom:.85rem}
.rp-labels{display:flex;justify-content:space-between;margin-bottom:.35rem}
.rp-labels span,.rp-labels strong{font-size:.76rem;color:var(--muted)}
.rp-labels strong{color:var(--text)}
.rp-bar-bg{background:var(--surface2);border-radius:4px;height:5px;border:1px solid var(--border);overflow:hidden}
.rp-bar{height:100%;border-radius:4px;background:linear-gradient(90deg,var(--accent),var(--easy));transition:width .4s}
.rm-pills{display:flex;gap:.4rem;flex-wrap:wrap;margin:1rem 0}
.rm-pill{padding:.28rem .75rem;border-radius:20px;border:1px solid var(--border);
  background:var(--surface2);color:var(--muted);cursor:pointer;font-size:.7rem;
  font-weight:700;text-transform:uppercase;letter-spacing:.05em;font-family:inherit;transition:all .15s}
.rm-pill:hover,.rm-pill.active{background:var(--accent);color:#fff;border-color:var(--accent)}
.rm-phase-lbl{font-size:.65rem;font-weight:700;color:var(--muted);text-transform:uppercase;
  letter-spacing:.1em;margin:.5rem 0 .35rem;padding-left:.1rem}
.rm-arrow{text-align:center;color:var(--muted);font-size:.75rem;margin:.15rem 0}
.rm-node{background:var(--surface2);border:1px solid var(--border);border-radius:10px;
  padding:.6rem .9rem;cursor:pointer;transition:all .15s;margin-bottom:.4rem}
.rm-node:hover{border-color:var(--accent);transform:translateY(-1px)}
.rm-node.open{border-color:var(--accent)}
.rm-top{display:flex;align-items:center;gap:.5rem}
.rm-icon{font-size:.9rem;flex-shrink:0}
.rm-title{font-weight:600;font-size:.84rem;flex:1}
.rm-badge{font-size:.64rem;font-weight:700;padding:.1rem .35rem;border-radius:8px;white-space:nowrap}
.rm-easy{background:rgba(26,127,55,.12);color:var(--easy)}
.rm-medium{background:rgba(154,103,0,.12);color:var(--medium)}
.rm-hard{background:rgba(209,36,47,.12);color:var(--hard)}
.rm-body{overflow:hidden;max-height:0;transition:max-height .3s ease;padding-top:0}
.rm-node.open .rm-body{max-height:600px;padding-top:.6rem;border-top:1px solid var(--border);margin-top:.6rem}
.rm-time{display:inline-flex;align-items:center;gap:.25rem;font-size:.65rem;font-weight:600;
  color:var(--muted);background:var(--surface);border:1px solid var(--border);
  border-radius:6px;padding:.1rem .38rem;margin-bottom:.45rem}
.rm-body h4{font-size:.73rem;font-weight:700;color:var(--accent);margin-bottom:.3rem}
.rm-steps{list-style:none;display:flex;flex-direction:column;gap:.25rem;margin-bottom:.5rem}
.rm-steps li{display:flex;align-items:baseline;gap:.4rem;font-size:.76rem;color:var(--muted);line-height:1.4}
.rm-sn{font-size:.62rem;font-weight:700;background:var(--surface);border:1px solid var(--border);
  border-radius:50%;min-width:15px;height:15px;display:inline-flex;align-items:center;
  justify-content:center;flex-shrink:0;color:var(--accent)}
.rm-chips{display:flex;flex-wrap:wrap;gap:.25rem}
.rm-chip{font-size:.64rem;padding:.1rem .38rem;border-radius:6px;
  border:1px solid var(--border);color:var(--muted);background:var(--surface)}
.rm-hidden{display:none}

::-webkit-scrollbar{width:6px;height:6px}
::-webkit-scrollbar-track{background:transparent}
::-webkit-scrollbar-thumb{background:var(--border);border-radius:3px}
@media(max-width:768px){.sidebar{display:none}.main{padding:1rem}}
"""

# ── JS ────────────────────────────────────────────────────────────────────────
JS = """
function router(){
  var h=window.location.hash.replace('#','');
  if(!h){showFirstSection();return;}
  if(h.indexOf('/')!==-1){var s=h.indexOf('/');showProblem(h.slice(0,s),h.slice(s+1));}
  else if(h.indexOf('section-')===0){showSection(h);}
  else{showFirstSection();}
}
function showFirstSection(){var f=document.querySelector('.topic-section');if(f)showSection(f.id);}
function navTo(id){window.location.hash=id;}
function showSection(id){
  document.getElementById('prob-pages').style.display='none';
  document.querySelectorAll('.topic-section').forEach(function(s){s.classList.remove('active');});
  document.querySelectorAll('.prob-page').forEach(function(p){p.classList.remove('active');});
  document.querySelectorAll('.sidebar li a').forEach(function(a){a.classList.remove('active');});
  var sec=document.getElementById(id);if(sec)sec.classList.add('active');
  var link=document.querySelector('.sidebar li a[href="#'+id+'"]');if(link)link.classList.add('active');
  window.scrollTo(0,0);
}
function showProblem(topic,slug){
  document.querySelectorAll('.topic-section').forEach(function(s){s.classList.remove('active');});
  document.querySelectorAll('.prob-page').forEach(function(p){p.classList.remove('active');});
  var pid=topic==='guides'?'guide-'+slug.toLowerCase().replace(/_/g,'-'):'prob-'+topic+'-'+slug;
  var page=document.getElementById(pid);
  if(!page){showSection('section-'+topic);return;}
  document.getElementById('prob-pages').style.display='block';
  page.classList.add('active');
  document.querySelectorAll('.sidebar li a').forEach(function(a){a.classList.remove('active');});
  var link=document.querySelector('.sidebar li a[href="#section-'+topic+'"]');if(link)link.classList.add('active');
  window.scrollTo(0,0);
}
window.addEventListener('DOMContentLoaded',router);
window.addEventListener('hashchange',router);
function switchTab(e,pid){
  var c=e.target.closest('.prob-page')||e.target.closest('.topic-section');
  c.querySelectorAll('.tab-btn').forEach(function(b){b.classList.remove('active');});
  c.querySelectorAll('.tab-panel').forEach(function(p){p.classList.remove('active');});
  e.target.classList.add('active');document.getElementById(pid).classList.add('active');
}
var activeDiff='all';
function filterDiff(diff,btn){
  activeDiff=diff;
  document.querySelectorAll('.filter-btn').forEach(function(b){b.classList.remove('active');});
  btn.classList.add('active');filterCards();
}
function filterCards(){
  var q=document.getElementById('search').value.toLowerCase();
  document.querySelectorAll('.topic-section.active .card').forEach(function(card){
    var name=(card.dataset.name||'').toLowerCase();
    var topic=(card.dataset.topic||'').toLowerCase();
    var diff=(card.dataset.diff||'').toLowerCase();
    var ok=(!q||name.includes(q)||topic.includes(q))&&(activeDiff==='all'||diff===activeDiff);
    card.classList.toggle('hidden',!ok);
  });
}
var rmOpen={};
function toggleRM(id){
  var node=document.getElementById(id);node.classList.toggle('open');
  if(node.classList.contains('open'))rmOpen[id]=1;else delete rmOpen[id];
  var done=Object.keys(rmOpen).length;
  document.getElementById('rm-pct').textContent=done+' / 17';
  document.getElementById('rm-bar').style.width=Math.round(done/17*100)+'%';
}
function filterRM(phase,btn){
  document.querySelectorAll('.rm-pill').forEach(function(b){b.classList.remove('active');});
  btn.classList.add('active');
  document.querySelectorAll('.rm-node').forEach(function(n){
    n.classList.toggle('rm-hidden',phase!=='all'&&n.id.indexOf(phase)===-1);
  });
}
"""

# ── Assemble page ─────────────────────────────────────────────────────────────
page = (
    "<!DOCTYPE html><html lang='en'><head>"
    "<meta charset='UTF-8'>"
    "<meta name='viewport' content='width=device-width,initial-scale=1.0'>"
    "<title>DSA Revision &mdash; Staff / Principal Engineer Prep</title>"
    "<style>" + CSS + "</style>"
    "</head><body>"
    "<div class='layout'>"

    "<nav class='sidebar'>"
    "<div class='sidebar-brand'>📘 DSA Revision</div>"
    "<ul>" + sidebar_html + "</ul>"
    "</nav>"

    "<main class='main'>"
    "<div class='topbar'>"
    "<div><div class='topbar-title'>DSA Revision</div>"
    "<div class='topbar-sub'>Staff / Principal Engineer Preparation</div></div>"
    "<div class='stats'>"
    "<div class='stat'><div class='stat-n'>" + str(total_problems) + "</div><div class='stat-l'>Problems</div></div>"
    "<div class='stat'><div class='stat-n'>15</div><div class='stat-l'>Guides</div></div>"
    "</div></div>"
    "<div class='controls'>"
    "<input class='search-box' type='text' id='search' placeholder='Search problems, topics, patterns...' oninput='filterCards()'>"
    "<button class='filter-btn active' onclick=\"filterDiff('all',this)\">All</button>"
    "<button class='filter-btn' onclick=\"filterDiff('easy',this)\">Easy</button>"
    "<button class='filter-btn' onclick=\"filterDiff('medium',this)\">Medium</button>"
    "<button class='filter-btn' onclick=\"filterDiff('hard',this)\">Hard</button>"
    "<button class='filter-btn' onclick=\"filterDiff('guide',this)\">Guides</button>"
    "</div>"

    + roadmap_html
    + sections_html
    + "<div id='prob-pages'>" + all_problem_pages + "</div>" +

    "</main></div>"
    "<script>" + JS + "</script>"
    "</body></html>"
)

(SITE / "index.html").write_text(page, encoding="utf-8")
print(f"Generated site/index.html ({len(page):,} bytes) — {total_problems} problems")
