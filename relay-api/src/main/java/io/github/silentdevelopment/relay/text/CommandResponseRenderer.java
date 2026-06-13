package io.github.silentdevelopment.relay.text;

import io.github.silentdevelopment.relay.command.Command;

import java.util.List;

public interface CommandResponseRenderer<S, R> {

    R renderUnknownCommand(S source);

    R renderNoHandler(S source);

    R renderInvalidUsage(S source, CommandText message, List<String> usages);

    R renderInvalidUsage(S source, CommandText message, String path, Command command);

    R renderRequirementFailure(S source, CommandText message);

    R renderAbort(S source, CommandText message);

    R renderSuggestions(S source, List<String> suggestions);

}