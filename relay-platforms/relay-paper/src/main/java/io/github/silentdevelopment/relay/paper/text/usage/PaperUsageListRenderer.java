package io.github.silentdevelopment.relay.paper.text.usage;

import net.kyori.adventure.text.Component;

import java.util.List;

public interface PaperUsageListRenderer {

    Component render(List<String> usages);

}