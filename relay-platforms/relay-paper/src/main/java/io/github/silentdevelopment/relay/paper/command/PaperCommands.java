package io.github.silentdevelopment.relay.paper.command;

import io.github.silentdevelopment.relay.paper.command.builder.PaperCommandBuilder;

public final class PaperCommands {

    private PaperCommands() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static PaperCommandBuilder literal(String name) {
        return PaperCommandBuilder.literal(name);
    }


}