package io.github.silentdevelopment.relay.paper.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class OfflinePlayerParser implements ArgumentParser<OfflinePlayer> {

    @Override
    public ParseResult<OfflinePlayer> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected an offline player.");
        }

        String value = reader.read();

        if (value.isBlank()) {
            return ParseResult.failure("Offline player cannot be blank.");
        }

        Player online = Bukkit.getPlayerExact(value);

        if (online != null) {
            return ParseResult.success(online);
        }

        try {
            UUID uuid = UUID.fromString(value);
            OfflinePlayer byUuid = Bukkit.getOfflinePlayer(uuid);

            if (byUuid.hasPlayedBefore() || byUuid.isOnline()) {
                return ParseResult.success(byUuid);
            }

            return ParseResult.failure("Unknown offline player: " + value);
        } catch (IllegalArgumentException ignored) {
        }

        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            String name = player.getName();

            if (name == null) {
                continue;
            }

            if (name.equalsIgnoreCase(value)) {
                return ParseResult.success(player);
            }
        }

        return ParseResult.failure("Unknown offline player: " + value);
    }

}