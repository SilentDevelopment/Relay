package io.github.silentdevelopment.relay.text;

public final class CommandTexts {

    private CommandTexts() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static CommandText unknownCommand() {
        return CommandText.of("unknown_command", "Unknown command.");
    }

    public static CommandText noHandler() {
        return CommandText.of("no_handler", "No handler is bound for this command.");
    }

    public static CommandText requirementFailed(String message) {
        return CommandText.of("requirement_failed", message);
    }

    public static CommandText aborted(String message) {
        return CommandText.of("aborted", message);
    }

    public static CommandText invalidUsage(String message) {
        return CommandText.of("invalid_usage", message);
    }

    public static CommandText invalidValue(String argument, String error) {
        return CommandText.of("invalid_value", "Invalid value for '{0}': {1}", argument, error);
    }

}