package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.core.util.DurationUtils;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class DurationUtilsTest {

    @Test
    void parsesSeconds() {
        assertEquals(Duration.ofSeconds(10), DurationUtils.parse("10s"));
    }

    @Test
    void parsesCompoundDuration() {
        assertEquals(Duration.ofHours(1).plusMinutes(30), DurationUtils.parse("1h 30m"));
    }

    @Test
    void parsesCommaSeparatedDuration() {
        assertEquals(Duration.ofDays(2).plusHours(4).plusMinutes(10), DurationUtils.parse("2d, 4h, 10m"));
    }

    @Test
    void parsesWeeksMonthsAndYearsUsingFixedDurations() {
        Duration expected = Duration.ofDays(365).plusDays(60).plusDays(7);
        assertEquals(expected, DurationUtils.parse("1y 2mo 1w"));
    }

    @Test
    void rejectsBlankInput() {
        assertThrows(IllegalArgumentException.class, () -> DurationUtils.parse(" "));
    }

    @Test
    void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> DurationUtils.parse("abc"));
    }

    @Test
    void rejectsTrailingGarbage() {
        assertThrows(IllegalArgumentException.class, () -> DurationUtils.parse("1h nope"));
    }

}