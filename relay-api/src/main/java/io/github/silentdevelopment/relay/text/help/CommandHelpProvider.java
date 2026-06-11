package io.github.silentdevelopment.relay.text.help;

import io.github.silentdevelopment.relay.command.Command;

import java.util.List;

public interface CommandHelpProvider<S> {

    List<CommandHelpEntry> build(S source, Command command);

    List<CommandHelpEntry> build(S source, Command command, String label);

}