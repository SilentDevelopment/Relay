package io.github.silentdevelopment.relay.core.argument.reader;

import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import io.github.silentdevelopment.relay.core.argument.reader.DefaultArgumentReaderFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultArgumentReaderFactoryTest {

    @Test
    void createReturnsReader() {
        DefaultArgumentReaderFactory factory = new DefaultArgumentReaderFactory();
        ArgumentReader reader = factory.create("one two");

        assertEquals(2, reader.size());
        assertEquals("one", reader.read());
        assertEquals("two", reader.read());
    }

}