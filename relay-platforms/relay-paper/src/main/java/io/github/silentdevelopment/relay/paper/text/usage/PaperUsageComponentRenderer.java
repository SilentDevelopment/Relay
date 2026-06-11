package io.github.silentdevelopment.relay.paper.text.usage;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.command.Signature;
import net.kyori.adventure.text.Component;

public interface PaperUsageComponentRenderer {

    Component render(String path, Command command, Signature signature);

}