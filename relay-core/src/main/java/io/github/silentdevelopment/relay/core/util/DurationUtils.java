package io.github.silentdevelopment.relay.core.util;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class DurationUtils {

    private static final Map<ChronoUnit, String> UNIT_PATTERNS = createUnitPatterns();
    private static final ChronoUnit[] UNITS = UNIT_PATTERNS.keySet().toArray(new ChronoUnit[0]);
    private static final String PATTERN_STRING = UNIT_PATTERNS.values().stream()
            .map(pattern -> "(?:(\\d+)\\s*" + pattern + "[,\\s]*)?")
            .collect(Collectors.joining());
    private static final Pattern PATTERN = Pattern.compile("^\\s*" + PATTERN_STRING + "\\s*$", Pattern.CASE_INSENSITIVE);

    private DurationUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static Duration parse(String input) {
        Objects.requireNonNull(input, "input");

        if (input.isBlank()) {
            throw new IllegalArgumentException("input cannot be blank.");
        }

        Matcher matcher = PATTERN.matcher(input);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Unable to parse duration: " + input);
        }

        Duration duration = Duration.ZERO;
        boolean matchedAny = false;

        for (int i = 0; i < UNITS.length; i++) {
            String group = matcher.group(i + 1);

            if (group == null || group.isBlank()) {
                continue;
            }

            long amount = Long.parseLong(group);

            if (amount < 0L) {
                throw new IllegalArgumentException("Duration values cannot be negative.");
            }

            if (amount == 0L) {
                matchedAny = true;
                continue;
            }

            duration = duration.plus(unitDuration(UNITS[i]).multipliedBy(amount));
            matchedAny = true;
        }

        if (!matchedAny) {
            throw new IllegalArgumentException("Unable to parse duration: " + input);
        }

        return duration;
    }

    public static Optional<Duration> parseSafely(String input) {
        try {
            return Optional.of(parse(input));
        } catch (RuntimeException ignored) {
            return Optional.empty();
        }
    }

    private static Duration unitDuration(ChronoUnit unit) {
        return switch (unit) {
            case YEARS -> Duration.ofDays(365);
            case MONTHS -> Duration.ofDays(30);
            case WEEKS -> Duration.ofDays(7);
            case DAYS -> Duration.ofDays(1);
            case HOURS -> Duration.ofHours(1);
            case MINUTES -> Duration.ofMinutes(1);
            case SECONDS -> Duration.ofSeconds(1);
            default -> throw new IllegalArgumentException("Unsupported unit: " + unit);
        };
    }

    private static Map<ChronoUnit, String> createUnitPatterns() {
        Map<ChronoUnit, String> patterns = new LinkedHashMap<>();
        patterns.put(ChronoUnit.YEARS, "y(?:ear)?s?");
        patterns.put(ChronoUnit.MONTHS, "mo(?:nth)?s?");
        patterns.put(ChronoUnit.WEEKS, "w(?:eek)?s?");
        patterns.put(ChronoUnit.DAYS, "d(?:ay)?s?");
        patterns.put(ChronoUnit.HOURS, "h(?:our|r)?s?");
        patterns.put(ChronoUnit.MINUTES, "m(?:in(?:ute)?)?s?");
        patterns.put(ChronoUnit.SECONDS, "s(?:ec(?:ond)?)?s?");
        return Collections.unmodifiableMap(patterns);
    }

}