import java.util.*;

public class Codec {

    // Encode: prefix each string with "len#"
    //
    // Key insight: length-prefix encoding is self-describing.
    // The decoder reads the length first, then reads EXACTLY that many chars.
    // No ambiguity regardless of what characters the string contains —
    // even if the string itself contains '#', digits, or looks like an encoding.
    //
    // Example:
    //   ["hello", "5#tricky", ""]
    //   → "5#hello8#5#tricky0#"
    //
    // The '#' after the length is a delimiter to separate the length digits
    // from the string content. Without it, "12" (length 12) and "1" followed
    // by "2..." would be ambiguous.
    public String encode(List<String> strs) {
        StringBuilder sb = new StringBuilder();
        for (String s : strs) {
            sb.append(s.length()).append('#').append(s);
        }
        return sb.toString();
    }

    // Decode: find '#', parse length, read exactly that many chars
    //
    // i always points to the start of the next "len#data" chunk.
    // j = indexOf('#', i) finds the delimiter.
    // len = parseInt(s[i..j))
    // string = s[j+1 .. j+1+len)
    // advance i past this chunk: i = j + 1 + len
    public List<String> decode(String s) {
        List<String> result = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            int j = s.indexOf('#', i);
            int len = Integer.parseInt(s.substring(i, j));
            result.add(s.substring(j + 1, j + 1 + len));
            i = j + 1 + len;
        }
        return result;
    }
}

/*
 * Complexity
 * ----------
 * Encode: O(n) — one StringBuilder append per string
 * Decode: O(n) — one pass; indexOf('#', i) advances i each time, O(n) total
 * Space:  O(n) — output proportional to total input length
 *
 * Why fixed delimiters fail:
 *   ["hello,world", "foo"] → "hello,world,foo" → ["hello","world","foo"]  WRONG
 *   Any single-char delimiter breaks if that char appears in a string.
 *   Length-prefix is the only delimiter-free, unambiguous encoding.
 *
 * Why '#' as separator doesn't cause ambiguity:
 *   The decoder NEVER scans for '#' to find the end of the string.
 *   It reads the length, then jumps directly to position j+1+len.
 *   The '#' within the string is consumed as part of the payload, not parsed.
 *
 * Trace — ["we", "say", ":", "yes"]
 * -----------------------------------
 * Encode: "2#we3#say1#:3#yes"
 *
 * Decode:
 *   i=0:  j=1,  len=2, s[2..4)  = "we",  i=4
 *   i=4:  j=5,  len=3, s[6..9)  = "say", i=9
 *   i=9:  j=10, len=1, s[11..12)= ":",   i=12
 *   i=12: j=13, len=3, s[14..17)= "yes", i=17 → done ✓
 *
 * Edge cases:
 *   [""]        → "0#"          → [""]          empty string, length=0
 *   ["#","##"]  → "1##2###"     → ["#","##"]    hash chars in strings
 *   ["5#trick"] → "8#5#trick"   → ["5#trick"]   string that looks like encoding
 *   []          → ""            → []            empty list
 *
 * Alternative: 4-byte fixed-width header (binary protocols)
 * ----------------------------------------------------------
 * Instead of ASCII length prefix, use 4 big-endian bytes per string.
 * This is how HTTP/2 frames, Kafka messages, and gRPC frames are structured.
 * More robust for binary data; variable ASCII prefix is simpler for text.
 */
