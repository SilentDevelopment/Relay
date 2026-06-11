package io.github.silentdevelopment.relay.core.argument.reader;

import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReaderFactory;

public final class DefaultArgumentReaderFactory implements ArgumentReaderFactory {

    @Override
    public ArgumentReader create(String input) {
        return DefaultArgumentReader.of(input);
    }

}