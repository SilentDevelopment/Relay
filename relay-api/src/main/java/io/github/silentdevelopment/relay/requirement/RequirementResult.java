package io.github.silentdevelopment.relay.requirement;

import io.github.silentdevelopment.relay.text.CommandText;

import java.util.Optional;

public final class RequirementResult {

    private final boolean allowed;
    private final CommandText message;

    private RequirementResult(boolean allowed, CommandText message) {
        this.allowed = allowed;
        this.message = message;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public boolean isDenied() {
        return !allowed;
    }

    public Optional<CommandText> getText() {
        return Optional.ofNullable(message);
    }

    /**
     * Kept for source compatibility with older integrations.
     */
    @Deprecated
    public Optional<String> getMessage() {
        return getText().map(CommandText::fallback);
    }

    public static RequirementResult allow() {
        return new RequirementResult(true, null);
    }

    public static RequirementResult deny(String message) {
        return deny(CommandText.of(message));
    }

    public static RequirementResult deny(CommandText message) {
        if (message == null) {
            throw new IllegalArgumentException("message cannot be null.");
        }

        return new RequirementResult(false, message);
    }
}