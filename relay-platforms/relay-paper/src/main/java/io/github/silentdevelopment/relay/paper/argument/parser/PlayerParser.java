package io.github.silentdevelopment.relay.paper.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class PlayerParser implements ArgumentParser<Player> {

    @Override
    public ParseResult<Player> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a player.");
        }

        String value = reader.read();

        if (value.isBlank()) {
            return ParseResult.failure("Player cannot be blank.");
        }

        Player exact = Bukkit.getPlayerExact(value);

        if (exact != null) {
            return ParseResult.success(exact);
        }

        Player player = Bukkit.getPlayer(value);
        if (player != null) {
            return ParseResult.success(player);
        }

        return ParseResult.failure("Unknown online player: " + value);
    }

}