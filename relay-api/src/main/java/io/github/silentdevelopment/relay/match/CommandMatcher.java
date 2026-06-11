package io.github.silentdevelopment.relay.match;

import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import io.github.silentdevelopment.relay.command.Command;

public interface CommandMatcher {

    CommandMatchResult match(Command command, ArgumentReader reader);

}