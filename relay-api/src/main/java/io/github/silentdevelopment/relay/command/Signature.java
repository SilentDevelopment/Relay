package io.github.silentdevelopment.relay.command;

import io.github.silentdevelopment.relay.argument.Argument;

import java.util.List;
import java.util.StringJoiner;

public record Signature(List<Argument<?>> arguments) {

    public Signature {
        if (arguments == null) {
            throw new IllegalArgumentException("arguments cannot be null.");
        }

        arguments = List.copyOf(arguments);
        validate(arguments);
    }

    public String usage() {
        StringJoiner joiner = new StringJoiner(" ");

        for (Argument<?> argument : this.arguments) {
            joiner.add(argument.usage());
        }

        return joiner.toString();
    }

    public static Signature of(List<Argument<?>> arguments) {
        return new Signature(arguments);
    }

    public static Signature of(Argument<?>... arguments) {
        if (arguments == null) {
            throw new IllegalArgumentException("arguments cannot be null.");
        }

        return new Signature(List.of(arguments));
    }

    private static void validate(List<Argument<?>> arguments) {
        boolean optionalSeen = false;
        boolean greedySeen = false;

        for (int i = 0; i < arguments.size(); i++) {
            Argument<?> argument = arguments.get(i);

            if (argument == null) {
                throw new IllegalArgumentException("Signature arguments cannot contain null.");
            }

            if (!argument.required()) {
                optionalSeen = true;
            } else if (optionalSeen) {
                throw new IllegalArgumentException("Required arguments cannot appear after optional arguments.");
            }

            if (!argument.greedy()) {
                continue;
            }

            if (greedySeen) {
                throw new IllegalArgumentException("A signature cannot contain more than one greedy argument.");
            }

            if (i != arguments.size() - 1) {
                throw new IllegalArgumentException("A greedy argument must be the last argument in a signature.");
            }

            greedySeen = true;
        }
    }

}