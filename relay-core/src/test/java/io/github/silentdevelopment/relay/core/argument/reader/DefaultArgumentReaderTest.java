package io.github.silentdevelopment.relay.core.argument.reader;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultArgumentReaderTest {

    @Test
    void tokenizesSimpleInput() {
        DefaultArgumentReader reader = DefaultArgumentReader.of("one two three");

        assertEquals(List.of("one", "two", "three"), reader.getTokens());
        assertEquals(3, reader.size());
    }

    @Test
    void tokenizesQuotedInput() {
        DefaultArgumentReader reader = DefaultArgumentReader.of("one \"two three\" four");

        assertEquals(List.of("one", "two three", "four"), reader.getTokens());
    }

    @Test
    void readsSequentially() {
        DefaultArgumentReader reader = DefaultArgumentReader.of("one two");

        assertTrue(reader.hasNext());
        assertEquals("one", reader.peek());
        assertEquals("one", reader.read());
        assertEquals("two", reader.read());
        assertFalse(reader.hasNext());
    }

    @Test
    void readRemainingJoinsRemainingTokens() {
        DefaultArgumentReader reader = DefaultArgumentReader.of("one two three");

        assertEquals("one", reader.read());
        assertEquals("two three", reader.readRemaining());
        assertFalse(reader.hasNext());
    }

    @Test
    void setCursorMovesReader() {
        DefaultArgumentReader reader = DefaultArgumentReader.of("one two three");
        reader.setCursor(2);

        assertEquals("three", reader.peek());
        assertEquals(2, reader.getCursor());
    }

    @Test
    void strictModeRejectsUnterminatedQuotes() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> DefaultArgumentReader.of("\"abc"));
        assertEquals("Input contains an unterminated quoted string.", ex.getMessage());
    }

    @Test
    void lenientModeAllowsUnterminatedQuotes() {
        DefaultArgumentReader reader = DefaultArgumentReader.ofLenient("\"abc");
        assertEquals(List.of("abc"), reader.getTokens());
    }

    @Test
    void strictModeRejectsDanglingEscape() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> DefaultArgumentReader.of("abc\\"));
        assertEquals("Input ends with an incomplete escape sequence.", ex.getMessage());
    }

    @Test
    void lenientModeKeepsDanglingEscape() {
        DefaultArgumentReader reader = DefaultArgumentReader.ofLenient("abc\\");
        assertEquals(List.of("abc\\"), reader.getTokens());
    }

    @Test
    void constructorRejectsNullTokenList() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new DefaultArgumentReader(null));
        assertEquals("tokens cannot be null.", ex.getMessage());
    }

    @Test
    void constructorRejectsNullTokenEntries() {
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("a");
        tokens.add(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new DefaultArgumentReader(tokens));
        assertEquals("tokens cannot contain null.", ex.getMessage());
    }

}