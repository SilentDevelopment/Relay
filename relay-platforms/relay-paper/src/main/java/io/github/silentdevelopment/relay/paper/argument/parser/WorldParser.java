package io.github.silentdevelopment.relay.paper.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import org.bukkit.Bukkit;
import org.bukkit.World;

public final class WorldParser implements ArgumentParser<World> {

    @Override
    public ParseResult<World> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a world.");
        }

        String value = reader.read();

        if (value.isBlank()) {
            return ParseResult.failure("World cannot be blank.");
        }

        World exact = Bukkit.getWorld(value);

        if (exact != null) {
            return ParseResult.success(exact);
        }

        for (World world : Bukkit.getWorlds()) {
            if (world.getName().equalsIgnoreCase(value)) {
                return ParseResult.success(world);
            }
        }

        return ParseResult.failure("Unknown world: " + value);
    }

}