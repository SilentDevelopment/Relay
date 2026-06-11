package io.github.silentdevelopment.relay.text.usage;

import io.github.silentdevelopment.relay.command.Command;

import java.util.List;

public interface CommandUsageFormatter {

    List<String> format(Command command);

    List<String> format(Command command, String label);

}