package net.saka1.joptparse.parser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Like StringScanner in Ruby. currently works only on Non-surrogate-pair.
 */
public class Scanner {
    private final String source;
    private int currentIndex;

    public Scanner(String source) {
        this.source = source;
        this.currentIndex = 0;
    }

    public Optional<String> scan(String str) {
        if (source.startsWith(str, currentIndex)) {
            currentIndex += str.length();
            return Optional.of(str);
        }
        return Optional.empty();
    }

    public Optional<String> scanRegexp(String pattern) {
        Matcher m = Pattern.compile(pattern).matcher(source);
        if (m.find(currentIndex) && m.start() == currentIndex) {
            currentIndex += m.group().length();
            return Optional.of(m.group());
        }
        return Optional.empty();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public boolean isEos() {
        return currentIndex == source.length();
    }
}
