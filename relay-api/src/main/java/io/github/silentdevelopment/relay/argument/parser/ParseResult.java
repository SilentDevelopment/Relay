package io.github.silentdevelopment.relay.argument.parser;

public final class ParseResult<T> {

    private final T value;
    private final String error;
    private final boolean success;

    private ParseResult(T value, String error, boolean success) {
        this.value = value;
        this.error = error;
        this.success = success;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public boolean isFailure() {
        return !this.success;
    }

    public T getValue() {
        if (!this.success) {
            throw new IllegalStateException("Cannot access value from a failed parse result.");
        }

        return this.value;
    }

    public String getError() {
        if (this.success) {
            throw new IllegalStateException("Cannot access error from a successful parse result.");
        }

        return this.error;
    }

    public static <T> ParseResult<T> success(T value) {
        return new ParseResult<>(value, null, true);
    }

    public static <T> ParseResult<T> failure(String error) {
        if (error == null || error.isBlank()) {
            throw new IllegalArgumentException("error cannot be null or blank.");
        }

        return new ParseResult<>(null, error, false);
    }

}