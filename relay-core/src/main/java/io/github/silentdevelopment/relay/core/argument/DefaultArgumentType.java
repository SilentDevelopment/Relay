package io.github.silentdevelopment.relay.core.argument;

import io.github.silentdevelopment.relay.argument.ArgumentType;
import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;

import java.util.Objects;

public final class DefaultArgumentType<T> implements ArgumentType<T> {

    private final String id;
    private final Class<T> valueType;
    private final ArgumentParser<T> parser;

    public DefaultArgumentType(String id, Class<T> valueType, ArgumentParser<T> parser) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id cannot be null or blank.");
        }

        this.id = id;
        this.valueType = Objects.requireNonNull(valueType, "valueType");
        this.parser = Objects.requireNonNull(parser, "parser");
    }

    @Override
    public String identifier() {
        return this.id;
    }

    @Override
    public Class<T> type() {
        return this.valueType;
    }

    @Override
    public ArgumentParser<T> parser() {
        return this.parser;
    }

}