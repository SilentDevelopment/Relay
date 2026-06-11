package io.github.silentdevelopment.relay.command.option;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class OptionParseResult {

    private final List<String> positionalTokens;
    private final Set<CommandOption> presentOptions;
    private final Map<ValueCommandOption<?>, Object> optionValues;
    private final String error;

    private OptionParseResult(List<String> positionalTokens, Set<CommandOption> presentOptions, Map<ValueCommandOption<?>, Object> optionValues, String error) {
        this.positionalTokens = positionalTokens;
        this.presentOptions = presentOptions;
        this.optionValues = optionValues;
        this.error = error;
    }

    public boolean isFailure() {
        return this.error != null;
    }

    public String getError() {
        if (this.error == null) {
            throw new IllegalStateException("Cannot access error from a successful option parse result.");
        }

        return this.error;
    }

    public List<String> getPositionalTokens() {
        return this.positionalTokens;
    }

    public Set<CommandOption> getPresentOptions() {
        return this.presentOptions;
    }

    public Map<ValueCommandOption<?>, Object> getOptionValues() {
        return this.optionValues;
    }

    public static OptionParseResult success(List<String> positionalTokens, Set<CommandOption> presentOptions, Map<ValueCommandOption<?>, Object> optionValues) {
        return new OptionParseResult(List.copyOf(positionalTokens), Set.copyOf(presentOptions), Map.copyOf(optionValues), null);
    }

    public static OptionParseResult failure(String error) {
        if (error == null || error.isBlank()) {
            throw new IllegalArgumentException("error cannot be null or blank.");
        }

        return new OptionParseResult(List.of(), Set.of(), Map.of(), error);
    }

}