package io.github.silentdevelopment.relay.paper.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import org.bukkit.GameMode;

import java.util.Locale;

public final class GameModeParser implements ArgumentParser<GameMode> {

    @Override
    public ParseResult<GameMode> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a game mode.");
        }

        String value = normalize(reader.read());

        for (GameMode gameMode : GameMode.values()) {
            if (gameMode.name().equalsIgnoreCase(value)) {
                return ParseResult.success(gameMode);
            }
        }

        return ParseResult.failure("Unknown game mode: " + value);
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
    }

}