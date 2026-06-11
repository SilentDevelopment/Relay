package io.github.silentdevelopment.relay.suggestion;

import java.util.List;

public interface SuggestionProvider<S> {

    List<String> suggest(SuggestionContext<S> context);

}