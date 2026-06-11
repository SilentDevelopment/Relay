package io.github.silentdevelopment.relay.argument;

import java.util.Objects;

public record Argument<T>(String name, ArgumentType<T> type, boolean required, boolean greedy) {

    public Argument {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be null or blank.");
        }

        type = Objects.requireNonNull(type, "type");
    }

    public boolean optional() {
        return !this.required;
    }

    public String usage() {
        String value = this.greedy ? this.name + "..." : this.name;
        return this.required ? "<" + value + ">" : "[" + value + "]";
    }

    public static <T> Argument<T> required(String name, ArgumentType<T> type) {
        return new Argument<>(name, type, true, false);
    }

    public static <T> Argument<T> optional(String name, ArgumentType<T> type) {
        return new Argument<>(name, type, false, false);
    }

    public static <T> Argument<T> greedyRequired(String name, ArgumentType<T> type) {
        return new Argument<>(name, type, true, true);
    }

    public static <T> Argument<T> greedyOptional(String name, ArgumentType<T> type) {
        return new Argument<>(name, type, false, true);
    }

}