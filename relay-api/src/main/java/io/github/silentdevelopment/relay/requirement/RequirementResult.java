package io.github.silentdevelopment.relay.requirement;

import java.util.Optional;

public final class RequirementResult {

    private final boolean allowed;
    private final String message;

    private RequirementResult(boolean allowed, String message) {
        this.allowed = allowed;
        this.message = message;
    }

    public boolean isAllowed() {
        return this.allowed;
    }

    public boolean isDenied() {
        return !this.allowed;
    }

    public Optional<String> getMessage() {
        return Optional.ofNullable(this.message);
    }

    public static RequirementResult allow() {
        return new RequirementResult(true, null);
    }

    public static RequirementResult deny(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message cannot be null or blank.");
        }

        return new RequirementResult(false, message);
    }

}