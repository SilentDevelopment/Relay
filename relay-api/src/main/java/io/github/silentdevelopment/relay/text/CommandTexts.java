package io.github.silentdevelopment.relay.text;

import java.util.Map;

public final class CommandTexts {

    private CommandTexts() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static CommandText unknownCommand() {
        return CommandText.of("relay.error.unknown-command", "Unknown command.");
    }

    public static CommandText noHandler() {
        return CommandText.of("relay.error.no-handler", "No handler is bound for this command.");
    }

    public static CommandText requirementFailed(String message) {
        return CommandText.of("relay.error.requirement-failed", message);
    }

    public static CommandText requirementFailed(String key, String fallback) {
        return CommandText.of(key, fallback);
    }

    public static CommandText requirementFailed(String key, String fallback, Map<String, Object> placeholders) {
        return CommandText.keyed(key, fallback, placeholders);
    }

    public static CommandText noPermission(String permission) {
        return CommandText.keyed(
                "relay.error.no-permission",
                "You do not have permission to use this command.",
                Map.of("permission", permission)
        );
    }

    public static CommandText playerOnly() {
        return CommandText.of("relay.error.player-only", "Only players can use this command.");
    }

    public static CommandText consoleOnly() {
        return CommandText.of("relay.error.console-only", "Only the console can use this command.");
    }

    public static CommandText aborted(String message) {
        return CommandText.of("relay.error.aborted", message);
    }

    public static CommandText invalidUsage(String message) {
        return CommandText.of("relay.error.invalid-usage", message);
    }

    public static CommandText invalidValue(String argument, String error) {
        return CommandText.keyed(
                "relay.error.invalid-value",
                "Invalid value for '{argument}': {error}",
                Map.of(
                        "argument", argument,
                        "error", error
                )
        );
    }

}