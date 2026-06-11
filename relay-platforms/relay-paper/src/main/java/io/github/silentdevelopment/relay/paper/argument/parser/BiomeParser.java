package io.github.silentdevelopment.relay.paper.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Biome;

import java.util.Locale;

public final class BiomeParser implements ArgumentParser<Biome> {

    @Override
    public ParseResult<Biome> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a biome.");
        }

        String value = reader.read();

        if (value.isBlank()) {
            return ParseResult.failure("Biome cannot be blank.");
        }

        Registry<Biome> registry = registry();
        NamespacedKey explicit = NamespacedKey.fromString(value);

        if (explicit != null) {
            Biome biome = registry.get(explicit);

            if (biome != null) {
                return ParseResult.success(biome);
            }
        }

        NamespacedKey minecraft = NamespacedKey.minecraft(normalize(value));
        Biome biome = registry.get(minecraft);

        if (biome != null) {
            return ParseResult.success(biome);
        }

        return ParseResult.failure("Unknown biome: " + value);
    }

    private static Registry<Biome> registry() {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT).replace(' ', '_').replace('-', '_');
    }

}