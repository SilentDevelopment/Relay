package io.github.silentdevelopment.relay.argument.reader;

import java.util.List;

public interface ArgumentReader {

    boolean hasNext();

    String peek();

    String read();

    String readRemaining();

    int getCursor();

    void setCursor(int cursor);

    int size();

    List<String> getTokens();

}