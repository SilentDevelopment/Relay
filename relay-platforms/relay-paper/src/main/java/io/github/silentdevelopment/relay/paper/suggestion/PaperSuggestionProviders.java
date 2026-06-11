package io.github.silentdevelopment.relay.paper.suggestion;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class PaperSuggestionProviders {

    private PaperSuggestionProviders() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static PaperSuggestionProvider onlinePlayers() {
        return context -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }

    public static PaperSuggestionProvider worlds() {
        return context -> Bukkit.getWorlds().stream().map(World::getName).toList();
    }

    public static PaperSuggestionProvider playerNames(Collection<? extends Player> players) {
        Objects.requireNonNull(players, "players");
        return context -> players.stream().map(Player::getName).toList();
    }

    public static PaperSuggestionProvider values(String... values) {
        if (values == null) {
            throw new IllegalArgumentException("values cannot be null.");
        }

        return values(List.of(values));
    }

    public static PaperSuggestionProvider values(Collection<String> values) {
        if (values == null) {
            throw new IllegalArgumentException("values cannot be null.");
        }

        List<String> suggestions = List.copyOf(values);
        return context -> suggestions;
    }

}
