# Encode and Decode Strings — Real-World Use Cases

Length-prefix encoding — the core of this problem — is the foundation of
virtually every binary network protocol in production.

---

## 1. HTTP/1.1 — Chunked Transfer Encoding

HTTP/1.1 chunked transfer encoding uses length-prefix framing to stream
a response of unknown total size. Each chunk is preceded by its size in
hexadecimal, followed by CRLF, then the chunk data, then another CRLF.
Transmission ends when a zero-length chunk is received.

```
HTTP chunked format:
  <hex-length>

  <chunk-data>

  ...
  0
          ← terminating zero-length chunk
  

```

This is structurally identical to our `len#data` encoding — the receiver
reads the length, consumes exactly that many bytes, then reads the next
length. No delimiter scanning, no ambiguity.

- **RFC 9112 §7.1** (current HTTP/1.1 spec):
  https://datatracker.ietf.org/doc/html/rfc9112#section-7.1
  "Each chunk is preceded by its size in bytes. Transmission ends when a
  zero-length chunk is received."

- **RFC 7230** (original, now obsoleted by RFC 9112):
  https://www.rfc-editor.org/rfc/rfc7230.html
  "Transfer-Encoding is primarily intended to accurately delimit a
  dynamically generated payload — a recipient MUST be able to parse the
  chunked transfer coding because it plays a crucial role in framing
  messages when the payload body size is not known in advance."

- **Wikipedia — Chunked Transfer Encoding:**
  https://en.wikipedia.org/wiki/Chunked_transfer_encoding

---

## 2. Protocol Buffers (protobuf) — Length-Delimited Fields (TLV)

Google's Protocol Buffers encode variable-length fields (strings, bytes,
embedded messages) using wire type 2: LEN (length-delimited). Each such
field is encoded as a varint length immediately after the field tag,
followed by exactly that many bytes of payload.

```
Protobuf encoding of string field "testing" (field number 2):
  Tag:    0x12  (field 2, wire type 2 = length-delimited)
  Length: 0x07  (varint 7)
  Data:   74 65 73 74 69 6e 67  ("testing" in UTF-8)
```

This is called **Tag-Length-Value (TLV)** encoding — the same pattern as
our solution, extended with a type tag before the length.

- **Official protobuf encoding documentation:**
  https://protobuf.dev/programming-guides/encoding/
  "The LEN wire type has a dynamic length, specified by a varint immediately
  after the tag, followed by the payload. Strings use wire type 2
  (length-delimited): the value is the byte count as a varint followed by
  the UTF-8 encoded string bytes."

- **Wire type table:**
  | Wire type | Used for |
  |-----------|----------|
  | 0 (VARINT) | int32, int64, bool, enum |
  | 1 (64-bit) | fixed64, double |
  | 2 (LEN) | string, bytes, embedded messages |
  | 5 (32-bit) | fixed32, float |

---

## 3. Redis RESP Protocol — Bulk Strings

Redis's serialisation protocol (RESP) encodes all string data as bulk
strings using a `$<length>
<data>
` format — a `$` marker, the
ASCII length, CRLF, exactly that many bytes of data, and a final CRLF.

```
RESP bulk string encoding:
  $6
foobar
     →  "foobar"
  $0

           →  ""  (empty string)
  $-1
              →  nil (null bulk string)
```

```
Redis command "SET foo bar" encoded as RESP array of bulk strings:
  *3
        ← array of 3 elements
  $3
SET

  $3
foo

  $3
bar

```

RESP is explicitly designed to be binary-safe because it uses prefixed
length — it never needs to scan for delimiters within the data.

- **Official Redis RESP specification:**
  https://redis.io/docs/latest/develop/reference/protocol-spec/
  "RESP is binary-safe and uses prefixed length to transfer bulk data
  so it does not require processing bulk data transferred from one
  process to another."

- **Redis specifications (GitHub):**
  https://github.com/redis/redis-specifications/blob/master/protocol/RESP2.md

---

## Pattern Summary — Length-Prefix Encoding in Production

All three protocols implement the same fundamental idea as this LeetCode problem:

| Protocol | Length format | Separator | Payload |
|----------|--------------|-----------|---------|
| Our solution | ASCII decimal | `#` | String chars |
| HTTP chunked | Hex + CRLF | `
` | Chunk bytes |
| Protobuf LEN | Varint | (none, immediately follows) | UTF-8 bytes |
| Redis RESP | `$` + ASCII decimal + CRLF | `
` | String bytes |
| gRPC framing | 4-byte big-endian | (none) | Protobuf message |
| Kafka message | 4-byte big-endian | (none) | UTF-8 bytes |

The core invariant in every case: **read the length, consume exactly that
many bytes, advance — never scan for a terminator**.

---

## Further Reading

- HTTP chunked encoding: https://datatracker.ietf.org/doc/html/rfc9112#section-7.1
- Protobuf encoding: https://protobuf.dev/programming-guides/encoding/
- Redis RESP spec: https://redis.io/docs/latest/develop/reference/protocol-spec/
- Wikipedia chunked transfer: https://en.wikipedia.org/wiki/Chunked_transfer_encoding
