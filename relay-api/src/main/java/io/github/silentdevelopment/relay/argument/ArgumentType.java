package io.github.silentdevelopment.relay.argument;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;

public interface ArgumentType<T> {

    String identifier();

    Class<T> type();

    ArgumentParser<T> parser();

}