package io.github.silentdevelopment.relay.command.option;

import java.util.List;

public interface CommandOption {

    String getName();

    List<String> getAliases();

    String getDescription();

}