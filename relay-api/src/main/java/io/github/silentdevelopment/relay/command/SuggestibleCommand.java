package io.github.silentdevelopment.relay.command;

import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.suggestion.SuggestionProvider;

import java.util.Map;

public interface SuggestibleCommand<S> extends Command {

    Map<Argument<?>, SuggestionProvider<S>> getSuggestionProviders();

}