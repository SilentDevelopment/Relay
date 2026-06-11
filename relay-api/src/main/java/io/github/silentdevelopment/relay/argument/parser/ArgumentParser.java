package io.github.silentdevelopment.relay.argument.parser;

import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;

public interface ArgumentParser<T> {

    ParseResult<T> parse(ArgumentReader reader);

}