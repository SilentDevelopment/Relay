package io.github.silentdevelopment.relay.suggestion;

import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.Signature;

import java.util.Objects;

public record SuggestionContext<S>(S source, Command command, Signature signature, Argument<?> argument, String input, String currentToken) {

    public SuggestionContext(S source, Command command, Signature signature, Argument<?> argument, String input, String currentToken) {
        this.source = Objects.requireNonNull(source, "source");
        this.command = Objects.requireNonNull(command, "command");
        this.signature = Objects.requireNonNull(signature, "signature");
        this.argument = Objects.requireNonNull(argument, "argument");
        this.input = input == null ? "" : input;
        this.currentToken = currentToken == null ? "" : currentToken;
    }

}