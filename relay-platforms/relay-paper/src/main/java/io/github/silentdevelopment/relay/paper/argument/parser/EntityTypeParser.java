package io.github.silentdevelopment.relay.paper.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import org.bukkit.entity.EntityType;

import java.util.Locale;

public final class EntityTypeParser implements ArgumentParser<EntityType> {

    @Override
    public ParseResult<EntityType> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected an entity type.");
        }

        String value = normalize(reader.read());

        for (EntityType entityType : EntityType.values()) {
            if (entityType.name().equalsIgnoreCase(value)) {
                return ParseResult.success(entityType);
            }
        }

        return ParseResult.failure("Unknown entity type: " + value);
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
    }

}
