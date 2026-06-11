package io.github.silentdevelopment.relay.paper.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;

import java.util.Locale;

public final class SoundParser implements ArgumentParser<Sound> {

    @Override
    public ParseResult<Sound> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a sound.");
        }

        String value = reader.read();

        if (value.isBlank()) {
            return ParseResult.failure("Sound cannot be blank.");
        }

        NamespacedKey explicit = NamespacedKey.fromString(value);

        if (explicit != null) {
            Sound sound = Registry.SOUND_EVENT.get(explicit);

            if (sound != null) {
                return ParseResult.success(sound);
            }
        }

        NamespacedKey minecraft = NamespacedKey.minecraft(normalize(value));
        Sound sound = Registry.SOUND_EVENT.get(minecraft);

        if (sound != null) {
            return ParseResult.success(sound);
        }

        return ParseResult.failure("Unknown sound: " + value);
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT).replace(' ', '_').replace('-', '_');
    }

}