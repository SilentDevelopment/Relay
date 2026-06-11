package io.github.silentdevelopment.relay.paper.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;

import java.util.Locale;

public final class AttributeParser implements ArgumentParser<Attribute> {

    @Override
    public ParseResult<Attribute> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected an attribute.");
        }

        String value = reader.read();

        if (value.isBlank()) {
            return ParseResult.failure("Attribute cannot be blank.");
        }

        NamespacedKey explicit = NamespacedKey.fromString(value);

        if (explicit != null) {
            Attribute attribute = Registry.ATTRIBUTE.get(explicit);

            if (attribute != null) {
                return ParseResult.success(attribute);
            }
        }

        NamespacedKey minecraft = NamespacedKey.minecraft(normalize(value));
        Attribute attribute = Registry.ATTRIBUTE.get(minecraft);

        if (attribute != null) {
            return ParseResult.success(attribute);
        }

        return ParseResult.failure("Unknown attribute: " + value);
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT).replace(' ', '_').replace('-', '_');
    }

}