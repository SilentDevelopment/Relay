package io.github.silentdevelopment.relay.paper.suggestion;

import io.github.silentdevelopment.relay.suggestion.SuggestionProvider;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface PaperSuggestionProvider extends SuggestionProvider<CommandSender> {
}
