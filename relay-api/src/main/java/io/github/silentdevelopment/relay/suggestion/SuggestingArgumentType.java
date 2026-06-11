package io.github.silentdevelopment.relay.suggestion;

import io.github.silentdevelopment.relay.argument.ArgumentType;

public interface SuggestingArgumentType<S, T> extends ArgumentType<T> {

    SuggestionProvider<S> getSuggestionProvider();

}