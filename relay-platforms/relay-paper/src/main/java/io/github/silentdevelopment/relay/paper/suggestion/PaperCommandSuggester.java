package io.github.silentdevelopment.relay.paper.suggestion;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.core.suggestion.DefaultCommandSuggester;
import io.github.silentdevelopment.relay.requirement.CommandAccessResolver;
import io.github.silentdevelopment.relay.suggestion.CommandSuggester;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Objects;

public final class PaperCommandSuggester implements CommandSuggester<CommandSender> {

    private final DefaultCommandSuggester<CommandSender> delegate;

    public PaperCommandSuggester() {
        this.delegate = new DefaultCommandSuggester<>();
    }

    public PaperCommandSuggester(CommandAccessResolver<CommandSender> accessResolver) {
        this.delegate = new DefaultCommandSuggester<>(accessResolver);
    }

    public PaperCommandSuggester(DefaultCommandSuggester<CommandSender> delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    public List<String> suggest(CommandSender source, Command command, String input) {
        return this.delegate.suggest(source, command, input);
    }

    public DefaultCommandSuggester<CommandSender> unwrap() {
        return this.delegate;
    }

}
