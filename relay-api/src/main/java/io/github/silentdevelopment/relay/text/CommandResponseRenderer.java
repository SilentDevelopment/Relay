package io.github.silentdevelopment.relay.text;

import java.util.List;

public interface CommandResponseRenderer<R> {

    R renderUnknownCommand();

    R renderNoHandler();

    R renderInvalidUsage(CommandText message, List<String> usages);

    R renderRequirementFailure(CommandText message);

    R renderAbort(CommandText message);

    R renderSuggestions(List<String> suggestions);

}