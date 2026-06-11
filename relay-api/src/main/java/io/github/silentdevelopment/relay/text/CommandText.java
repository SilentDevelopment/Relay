package io.github.silentdevelopment.relay.text;

import java.util.List;
import java.util.Objects;

public record CommandText(String key, String fallback, List<Object> arguments) {

    public CommandText {
        fallback = Objects.requireNonNull(fallback, "fallback");

        if (fallback.isBlank()) {
            throw new IllegalArgumentException("fallback cannot be blank.");
        }

        if (key != null && key.isBlank()) {
            throw new IllegalArgumentException("key cannot be blank.");
        }

        arguments = arguments == null ? List.of() : List.copyOf(arguments);
    }

    public static CommandText of(String key, String fallback) {
        return new CommandText(key, fallback, List.of());
    }

    public static CommandText of(String key, String fallback, Object... arguments) {
        return new CommandText(key, fallback, List.of(arguments));
    }

    public static CommandText of(String fallback) {
        return new CommandText(null, fallback, List.of());
    }

    public static CommandText of(String fallback, Object... arguments) {
        return new CommandText(null, fallback, List.of(arguments));
    }

}