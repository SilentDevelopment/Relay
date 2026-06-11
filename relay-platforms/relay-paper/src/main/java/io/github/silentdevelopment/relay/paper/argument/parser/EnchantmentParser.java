package io.github.silentdevelopment.relay.paper.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;

import java.util.Locale;

public final class EnchantmentParser implements ArgumentParser<Enchantment> {

    public static Registry<Enchantment> enchantmentRegistry() {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    }

    @Override
    public ParseResult<Enchantment> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected an enchantment.");
        }

        String value = reader.read();

        if (value.isBlank()) {
            return ParseResult.failure("Enchantment cannot be blank.");
        }

        NamespacedKey explicit = NamespacedKey.fromString(value);

        if (explicit != null) {
            Enchantment byExplicitKey = enchantmentRegistry().get(explicit);

            if (byExplicitKey != null) {
                return ParseResult.success(byExplicitKey);
            }
        }

        NamespacedKey minecraft = NamespacedKey.minecraft(value.toLowerCase(Locale.ROOT));
        Enchantment byMinecraftKey = enchantmentRegistry().get(minecraft);

        if (byMinecraftKey != null) {
            return ParseResult.success(byMinecraftKey);
        }

        return ParseResult.failure("Unknown enchantment: " + value);
    }

}