package io.github.silentdevelopment.relay.text;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record CommandText(
        String key,
        String fallback,
        List<Object> arguments,
        Map<String, Object> placeholders
) {

    public CommandText {
        fallback = Objects.requireNonNull(fallback, "fallback");

        if (fallback.isBlank()) {
            throw new IllegalArgumentException("fallback cannot be blank.");
        }

        if (key != null && key.isBlank()) {
            throw new IllegalArgumentException("key cannot be blank.");
        }

        arguments = arguments == null ? List.of() : List.copyOf(arguments);
        placeholders = placeholders == null ? Map.of() : Map.copyOf(placeholders);
    }

    public static CommandText of(String key, String fallback) {
        return new CommandText(key, fallback, List.of(), Map.of());
    }

    public static CommandText of(String key, String fallback, Object... arguments) {
        return new CommandText(key, fallback, List.of(arguments), Map.of());
    }

    public static CommandText of(String fallback) {
        return new CommandText(null, fallback, List.of(), Map.of());
    }

    public static CommandText of(String fallback, Object... arguments) {
        return new CommandText(null, fallback, List.of(arguments), Map.of());
    }

    public static CommandText keyed(String key, String fallback, Map<String, Object> placeholders) {
        return new CommandText(key, fallback, List.of(), placeholders);
    }

    public CommandText withPlaceholder(String key, Object value) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");

        Map<String, Object> updated = new LinkedHashMap<>(this.placeholders);
        updated.put(key, value);

        return new CommandText(this.key, this.fallback, this.arguments, updated);
    }

    public CommandText withPlaceholders(Map<String, Object> placeholders) {
        Objects.requireNonNull(placeholders, "placeholders");

        Map<String, Object> updated = new LinkedHashMap<>(this.placeholders);
        updated.putAll(placeholders);

        return new CommandText(this.key, this.fallback, this.arguments, updated);
    }

}