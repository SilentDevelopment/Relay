package io.github.silentdevelopment.relay.dispatch;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.Signature;
import io.github.silentdevelopment.relay.text.CommandText;

import java.util.Objects;
import java.util.Optional;

public final class CommandDispatchResult {

    private final CommandDispatchStatus status;
    private final Command command;
    private final Signature signature;
    private final CommandText message;

    private CommandDispatchResult(CommandDispatchStatus status, Command command, Signature signature, CommandText message) {
        this.status = Objects.requireNonNull(status, "status");
        this.command = command;
        this.signature = signature;
        this.message = message;
    }

    public CommandDispatchStatus getStatus() {
        return this.status;
    }

    public boolean isSuccess() {
        return this.status == CommandDispatchStatus.SUCCESS;
    }

    public Optional<Command> getCommand() {
        return Optional.ofNullable(this.command);
    }

    public Optional<Signature> getSignature() {
        return Optional.ofNullable(this.signature);
    }


    public Optional<CommandText> getMessage() {
        return Optional.ofNullable(this.message);
    }

    public static CommandDispatchResult success(Command command, Signature signature) {
        return new CommandDispatchResult(CommandDispatchStatus.SUCCESS, Objects.requireNonNull(command, "command"), Objects.requireNonNull(signature, "signature"), null);
    }

    public static CommandDispatchResult unknownCommand() {
        return new CommandDispatchResult(CommandDispatchStatus.UNKNOWN_COMMAND, null, null, null);
    }

    public static CommandDispatchResult invalidUsage(Command command, Signature signature, CommandText message) {
        return new CommandDispatchResult(CommandDispatchStatus.INVALID_USAGE, Objects.requireNonNull(command, "command"), signature, Objects.requireNonNull(message, "message"));
    }

    public static CommandDispatchResult requirementFailed(Command command, Signature signature, CommandText message) {
        return new CommandDispatchResult(CommandDispatchStatus.REQUIREMENT_FAILED, Objects.requireNonNull(command, "command"), Objects.requireNonNull(signature, "signature"), Objects.requireNonNull(message, "message"));
    }

    public static CommandDispatchResult aborted(Command command, Signature signature) {
        return new CommandDispatchResult(CommandDispatchStatus.ABORTED, Objects.requireNonNull(command, "command"), Objects.requireNonNull(signature, "signature"), null);
    }

    public static CommandDispatchResult aborted(Command command, Signature signature, CommandText message) {
        return new CommandDispatchResult(CommandDispatchStatus.ABORTED, Objects.requireNonNull(command, "command"), Objects.requireNonNull(signature, "signature"), Objects.requireNonNull(message, "message"));
    }

    public static CommandDispatchResult noHandler(Command command, Signature signature) {
        return new CommandDispatchResult(CommandDispatchStatus.NO_HANDLER, Objects.requireNonNull(command, "command"), Objects.requireNonNull(signature, "signature"), null);
    }

}