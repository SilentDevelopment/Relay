package io.github.silentdevelopment.relay.core.argument.reader;

import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public final class DefaultArgumentReader implements ArgumentReader {

    private final List<String> tokens;
    private int cursor;

    public DefaultArgumentReader(List<String> tokens) {
        if (tokens == null) {
            throw new IllegalArgumentException("tokens cannot be null.");
        }

        validateTokens(tokens);
        this.tokens = List.copyOf(tokens);
        this.cursor = 0;
    }

    public static DefaultArgumentReader of(List<String> tokens) {
        return new DefaultArgumentReader(tokens);
    }

    public static DefaultArgumentReader of(String input) {
        if (input == null || input.isBlank()) {
            return new DefaultArgumentReader(List.of());
        }

        return new DefaultArgumentReader(tokenize(input, false));
    }

    public static DefaultArgumentReader ofLenient(String input) {
        if (input == null || input.isBlank()) {
            return new DefaultArgumentReader(List.of());
        }

        return new DefaultArgumentReader(tokenize(input, true));
    }

    @Override
    public boolean hasNext() {
        return this.cursor < this.tokens.size();
    }

    @Override
    public String peek() {
        if (!hasNext()) {
            throw new IllegalStateException("No more tokens are available.");
        }

        return this.tokens.get(this.cursor);
    }

    @Override
    public String read() {
        if (!hasNext()) {
            throw new IllegalStateException("No more tokens are available.");
        }

        return this.tokens.get(this.cursor++);
    }

    @Override
    public String readRemaining() {
        if (!hasNext()) {
            return "";
        }

        StringJoiner joiner = new StringJoiner(" ");

        while (hasNext()) {
            joiner.add(read());
        }

        return joiner.toString();
    }

    @Override
    public int getCursor() {
        return this.cursor;
    }

    @Override
    public void setCursor(int cursor) {
        if (cursor < 0 || cursor > this.tokens.size()) {
            throw new IllegalArgumentException("Cursor out of bounds: " + cursor);
        }

        this.cursor = cursor;
    }

    @Override
    public int size() {
        return this.tokens.size();
    }

    @Override
    public List<String> getTokens() {
        return this.tokens;
    }

    private static List<String> tokenize(String input, boolean lenient) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean escaped = false;
        boolean tokenStarted = false;
        Character quote = null;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (escaped) {
                current.append(c);
                escaped = false;
                tokenStarted = true;
                continue;
            }

            if (c == '\\') {
                escaped = true;
                tokenStarted = true;
                continue;
            }

            if (quote != null) {
                if (c == quote) {
                    quote = null;
                    continue;
                }

                current.append(c);
                tokenStarted = true;
                continue;
            }

            if (c == '"' || c == '\'') {
                quote = c;
                tokenStarted = true;
                continue;
            }

            if (Character.isWhitespace(c)) {
                addToken(tokens, current, tokenStarted);
                tokenStarted = false;
                continue;
            }

            current.append(c);
            tokenStarted = true;
        }

        if (escaped) {
            if (!lenient) {
                throw new IllegalArgumentException("Input ends with an incomplete escape sequence.");
            }

            current.append('\\');
            tokenStarted = true;
        }

        if (quote != null && !lenient) {
            throw new IllegalArgumentException("Input contains an unterminated quoted string.");
        }

        addToken(tokens, current, tokenStarted);
        return List.copyOf(tokens);
    }

    private static void addToken(List<String> tokens, StringBuilder current, boolean tokenStarted) {
        if (!tokenStarted) {
            return;
        }

        tokens.add(current.toString());
        current.setLength(0);
    }

    private static void validateTokens(List<String> tokens) {
        for (String token : tokens) {
            if (token == null) {
                throw new IllegalArgumentException("tokens cannot contain null.");
            }
        }
    }

}