package io.github.silentdevelopment.relay.text.help;

import java.util.List;

public interface CommandHelpRenderer<R> {

    R render(CommandHelpEntry entry);

    R renderAll(List<CommandHelpEntry> entries);

}