package io.github.silentdevelopment.relay.paper.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;

import java.util.Locale;

public final class PotionEffectTypeParser implements ArgumentParser<PotionEffectType> {

    @Override
    public ParseResult<PotionEffectType> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a potion effect type.");
        }

        String value = reader.read();

        if (value.isBlank()) {
            return ParseResult.failure("Potion effect type cannot be blank.");
        }

        Registry<PotionEffectType> registry = registry();
        NamespacedKey explicit = NamespacedKey.fromString(value);

        if (explicit != null) {
            PotionEffectType type = registry.get(explicit);

            if (type != null) {
                return ParseResult.success(type);
            }
        }

        NamespacedKey minecraft = NamespacedKey.minecraft(normalize(value));
        PotionEffectType type = registry.get(minecraft);

        if (type != null) {
            return ParseResult.success(type);
        }

        return ParseResult.failure("Unknown potion effect type: " + value);
    }

    private static Registry<PotionEffectType> registry() {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.MOB_EFFECT);
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT).replace(' ', '_').replace('-', '_');
    }

}