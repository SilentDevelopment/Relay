package io.github.silentdevelopment.relay.paper.text.help;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.core.text.help.DefaultCommandHelpProvider;
import io.github.silentdevelopment.relay.requirement.CommandAccessResolver;
import io.github.silentdevelopment.relay.text.help.CommandHelpEntry;
import io.github.silentdevelopment.relay.text.help.CommandHelpProvider;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Objects;

public final class PaperCommandHelpProvider implements CommandHelpProvider<CommandSender> {

    private final DefaultCommandHelpProvider<CommandSender> delegate;

    public PaperCommandHelpProvider() {
        this.delegate = new DefaultCommandHelpProvider<>();
    }

    public PaperCommandHelpProvider(CommandAccessResolver<CommandSender> accessResolver) {
        this.delegate = new DefaultCommandHelpProvider<>(accessResolver);
    }

    public PaperCommandHelpProvider(DefaultCommandHelpProvider<CommandSender> delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    public List<CommandHelpEntry> build(CommandSender source, Command command) {
        return this.delegate.build(source, command);
    }

    public List<CommandHelpEntry> build(CommandSender source, Command command, String label) {
        return this.delegate.build(source, command, label);
    }

    public DefaultCommandHelpProvider<CommandSender> unwrap() {
        return this.delegate;
    }

}
