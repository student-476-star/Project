package com.netguard.ids.engine;

import com.netguard.ids.model.Rule;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class SignatureEngine {
    private final Trie trie;
    private final Map<String, Rule> keywordToRule = new HashMap<>();

    public SignatureEngine(List<Rule> rules) {
        Trie.TrieBuilder b = Trie.builder();
        for (Rule r : rules) {
            if (!r.isEnabled()) continue;
            if (r.getPattern_type() == null || r.getPattern_type().equalsIgnoreCase("string")) {
                String pat = r.isCase_sensitive() ? r.getPattern() : r.getPattern().toLowerCase(Locale.ROOT);
                b.addKeyword(pat);
                keywordToRule.put(pat, r);
            }
        }
        trie = b.caseInsensitive().removeOverlaps().build();
    }

    public List<MatchResult> match(byte[] payload) {
        String text = new String(payload, StandardCharsets.UTF_8);
        Collection<Emit> emits = trie.parseText(text);
        List<MatchResult> res = new ArrayList<>();
        for (Emit e : emits) {
            String kw = e.getKeyword();
            Rule r = keywordToRule.get(kw);
            if (r == null) {
                // try fallback: kw as-is
                continue;
            }
            res.add(new MatchResult(r, e.getStart(), e.getEnd(), text.substring(Math.max(0, e.getStart()-20), Math.min(text.length(), e.getEnd()+20))));
        }
        return res;
    }

    public static class MatchResult {
        public final Rule rule;
        public final int start;
        public final int end;
        public final String snippet;

        public MatchResult(Rule rule, int start, int end, String snippet) {
            this.rule = rule;
            this.start = start;
            this.end = end;
            this.snippet = snippet;
        }
    }
}
