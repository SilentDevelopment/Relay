package io.github.silentdevelopment.relay.core.argument;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.suggestion.SuggestingArgumentType;
import io.github.silentdevelopment.relay.suggestion.SuggestionProvider;

import java.util.Objects;

public final class DefaultSuggestingArgumentType<S, T> implements SuggestingArgumentType<S, T> {

    private final String identifier;
    private final Class<T> type;
    private final ArgumentParser<T> parser;
    private final SuggestionProvider<S> suggestionProvider;

    public DefaultSuggestingArgumentType(String identifier, Class<T> type, ArgumentParser<T> parser, SuggestionProvider<S> suggestionProvider) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("identifier cannot be null or blank.");
        }

        this.identifier = identifier;
        this.type = Objects.requireNonNull(type, "type");
        this.parser = Objects.requireNonNull(parser, "parser");
        this.suggestionProvider = Objects.requireNonNull(suggestionProvider, "suggestionProvider");
    }

    @Override
    public String identifier() {
        return this.identifier;
    }

    @Override
    public Class<T> type() {
        return this.type;
    }

    @Override
    public ArgumentParser<T> parser() {
        return this.parser;
    }

    @Override
    public SuggestionProvider<S> getSuggestionProvider() {
        return this.suggestionProvider;
    }

}