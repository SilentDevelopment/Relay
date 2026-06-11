package io.github.silentdevelopment.relay.paper.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import org.bukkit.Material;

public final class MaterialParser implements ArgumentParser<Material> {

    @Override
    public ParseResult<Material> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a material.");
        }

        String value = reader.read();

        if (value.isBlank()) {
            return ParseResult.failure("Material cannot be blank.");
        }

        Material material = Material.matchMaterial(value, true);

        if (material != null) {
            return ParseResult.success(material);
        }

        return ParseResult.failure("Unknown material: " + value);
    }

}