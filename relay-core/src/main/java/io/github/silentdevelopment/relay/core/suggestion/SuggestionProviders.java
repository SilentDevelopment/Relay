package io.github.silentdevelopment.relay.core.suggestion;

import io.github.silentdevelopment.relay.suggestion.SuggestionProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class SuggestionProviders {

    private SuggestionProviders() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static <S> SuggestionProvider<S> values(String... values) {
        if (values == null) {
            throw new IllegalArgumentException("values cannot be null.");
        }

        return values(Arrays.asList(values));
    }

    public static <S> SuggestionProvider<S> values(Collection<String> values) {
        if (values == null) {
            throw new IllegalArgumentException("values cannot be null.");
        }

        List<String> suggestions = List.copyOf(values);
        return context -> suggestions;
    }

}