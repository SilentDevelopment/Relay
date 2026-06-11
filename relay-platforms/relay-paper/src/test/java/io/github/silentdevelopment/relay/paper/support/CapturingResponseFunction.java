package io.github.silentdevelopment.relay.paper.support;

import io.github.silentdevelopment.relay.paper.adapter.PaperResponseFunction;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public final class CapturingResponseFunction implements PaperResponseFunction {

    private final List<Component> messages = new ArrayList<>();

    @Override
    public void send(CommandSender sender, Component message) {
        this.messages.add(message);
    }

    public int size() {
        return this.messages.size();
    }

    public List<Component> messages() {
        return List.copyOf(this.messages);
    }

}
