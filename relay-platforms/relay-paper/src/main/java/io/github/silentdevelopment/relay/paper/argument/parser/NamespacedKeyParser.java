package io.github.silentdevelopment.relay.paper.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import org.bukkit.NamespacedKey;

public final class NamespacedKeyParser implements ArgumentParser<NamespacedKey> {

    @Override
    public ParseResult<NamespacedKey> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a namespaced key.");
        }

        String value = reader.read();

        if (value.isBlank()) {
            return ParseResult.failure("Namespaced key cannot be blank.");
        }

        NamespacedKey key = NamespacedKey.fromString(value);

        if (key != null) {
            return ParseResult.success(key);
        }

        return ParseResult.failure("Expected a valid namespaced key like minecraft:stone.");
    }

}