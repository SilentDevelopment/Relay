package io.github.silentdevelopment.relay.suggestion;

import io.github.silentdevelopment.relay.command.Command;

import java.util.List;

public interface CommandSuggester<S> {

    List<String> suggest(S source, Command command, String input);

}