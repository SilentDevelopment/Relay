package io.github.silentdevelopment.relay.match;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.Signature;

import java.util.Objects;
import java.util.Optional;

public final class CommandMatchResult {

    private final CommandMatch match;
    private final Command command;
    private final Signature signature;
    private final String error;

    private CommandMatchResult(CommandMatch match, Command command, Signature signature, String error) {
        this.match = match;
        this.command = command;
        this.signature = signature;
        this.error = error;
    }

    public boolean isSuccess() {
        return this.match != null;
    }

    public boolean isFailure() {
        return !this.isSuccess();
    }

    public CommandMatch getMatch() {
        if (this.match == null) {
            throw new IllegalStateException("Cannot access match from a failed command match result.");
        }

        return this.match;
    }

    public Optional<Command> getCommand() {
        return this.match != null ? Optional.of(this.match.getCommand()) : Optional.ofNullable(this.command);
    }

    public Optional<Signature> getSignature() {
        return this.match != null ? Optional.of(this.match.getSignature()) : Optional.ofNullable(this.signature);
    }

    public Optional<String> getError() {
        return Optional.ofNullable(this.error);
    }

    public static CommandMatchResult success(CommandMatch match) {
        return new CommandMatchResult(Objects.requireNonNull(match, "match"), null, null, null);
    }

    public static CommandMatchResult failure(Command command, Signature signature, String error) {
        if (error == null || error.isBlank()) {
            throw new IllegalArgumentException("error cannot be null or blank.");
        }

        return new CommandMatchResult(
                null,
                Objects.requireNonNull(command, "command"),
                signature,
                error
        );
    }

}