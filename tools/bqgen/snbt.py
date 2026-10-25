"""Minimal SNBT parser that emits BetterQuesting's typed-JSON NBT form.

BetterQuesting stores NBT as JSON objects whose keys carry the NBT type id
("Count:3", "tag:10", ...). Quest authors write NBT in the familiar Minecraft
command syntax instead, e.g. ``{display:{Name:"Espada"},ench:[{id:16s,lvl:5s}]}``.

Type ids: 1 byte, 2 short, 3 int, 4 long, 5 float, 6 double, 7 byte[],
8 string, 9 list, 10 compound, 11 int[].
"""

import re

_NUM = re.compile(r'[-+]?(\d+\.?\d*|\.\d+)([eE][-+]?\d+)?[bBsSlLfFdD]?')


class SnbtError(ValueError):
    pass


class _Parser:
    def __init__(self, text):
        self.s = text
        self.i = 0

    def ws(self):
        while self.i < len(self.s) and self.s[self.i] in ' \t\r\n':
            self.i += 1

    def peek(self):
        self.ws()
        return self.s[self.i] if self.i < len(self.s) else ''

    def expect(self, ch):
        if self.peek() != ch:
            raise SnbtError('expected %r at %d in %r' % (ch, self.i, self.s))
        self.i += 1

    def value(self):
        c = self.peek()
        if c == '{':
            return self.compound()
        if c == '[':
            return self.array()
        if c in '"\'':
            return 8, self.quoted()
        return self.scalar()

    def compound(self):
        self.expect('{')
        out = {}
        if self.peek() == '}':
            self.i += 1
            return 10, out
        while True:
            key = self.quoted() if self.peek() in '"\'' else self.bare()
            self.expect(':')
            t, v = self.value()
            out['%s:%d' % (key, t)] = v
            c = self.peek()
            self.i += 1
            if c == '}':
                return 10, out
            if c != ',':
                raise SnbtError('bad compound near %d in %r' % (self.i, self.s))

    def array(self):
        self.expect('[')
        typed = None
        m = re.match(r'\s*([BIL]);', self.s[self.i:])
        if m:
            typed = m.group(1)
            self.i += m.end()
        items = []
        if self.peek() == ']':
            self.i += 1
        else:
            while True:
                items.append(self.value())
                c = self.peek()
                self.i += 1
                if c == ']':
                    break
                if c != ',':
                    raise SnbtError('bad list near %d in %r' % (self.i, self.s))
        if typed == 'I':
            return 11, [v for _, v in items]
        if typed == 'B':
            return 7, [v for _, v in items]
        out = {}
        for n, (t, v) in enumerate(items):
            out['%d:%d' % (n, t)] = v
        return 9, out

    def quoted(self):
        q = self.s[self.i]
        self.i += 1
        buf = []
        while self.i < len(self.s):
            ch = self.s[self.i]
            if ch == '\\':
                buf.append(self.s[self.i + 1])
                self.i += 2
                continue
            if ch == q:
                self.i += 1
                return ''.join(buf)
            buf.append(ch)
            self.i += 1
        raise SnbtError('unterminated string in %r' % self.s)

    def bare(self):
        self.ws()
        m = re.match(r'[A-Za-z0-9_.+\-]+', self.s[self.i:])
        if not m:
            raise SnbtError('bad key at %d in %r' % (self.i, self.s))
        self.i += m.end()
        return m.group(0)

    def scalar(self):
        self.ws()
        m = re.match(r'[^,}\]\s]+', self.s[self.i:])
        if not m:
            raise SnbtError('bad value at %d in %r' % (self.i, self.s))
        tok = m.group(0)
        self.i += m.end()
        if tok in ('true', 'false'):
            return 1, 1 if tok == 'true' else 0
        if _NUM.fullmatch(tok):
            suf = tok[-1].lower()
            if suf == 'b':
                return 1, int(tok[:-1])
            if suf == 's':
                return 2, int(tok[:-1])
            if suf == 'l':
                return 4, int(tok[:-1])
            if suf == 'f':
                return 5, float(tok[:-1])
            if suf == 'd':
                return 6, float(tok[:-1])
            if '.' in tok or 'e' in tok.lower():
                return 6, float(tok)
            return 3, int(tok)
        return 8, tok


def parse(text):
    """Parse an SNBT compound and return BetterQuesting typed JSON (dict)."""
    p = _Parser(text)
    t, v = p.value()
    if t != 10:
        raise SnbtError('top-level SNBT must be a compound: %r' % text)
    if p.peek():
        raise SnbtError('trailing data in %r' % text)
    return v
