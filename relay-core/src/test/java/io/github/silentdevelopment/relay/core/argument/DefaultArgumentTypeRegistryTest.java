package io.github.silentdevelopment.relay.core.argument;

import io.github.silentdevelopment.relay.argument.ArgumentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultArgumentTypeRegistryTest {

    @Test
    void defaultRegistryContainsBuiltins() {
        DefaultArgumentTypeRegistry registry = DefaultArgumentTypeRegistry.createDefault();

        assertTrue(registry.find("string").isPresent());
        assertTrue(registry.find("greedy_string").isPresent());
        assertTrue(registry.find("boolean").isPresent());
        assertTrue(registry.find("integer").isPresent());
    }

    @Test
    void lookupByClassReturnsDefault() {
        DefaultArgumentTypeRegistry registry = DefaultArgumentTypeRegistry.createDefault();

        ArgumentType<String> type = registry.findDefault(String.class).orElseThrow();
        assertEquals("string", type.identifier());
    }

    @Test
    void lookupByClassReturnsAllMatching() {
        DefaultArgumentTypeRegistry registry = DefaultArgumentTypeRegistry.createDefault();

        List<ArgumentType<String>> types = registry.findAll(String.class);
        assertEquals(2, types.size());
        assertEquals(List.of("string", "greedy_string"), types.stream().map(ArgumentType::identifier).toList());
    }

    @Test
    void duplicateIdentifierIsRejected() {
        DefaultArgumentTypeRegistry registry = new DefaultArgumentTypeRegistry();
        registry.register(ArgumentTypes.STRING);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> registry.register(ArgumentTypes.STRING));
        assertEquals("An argument type with id 'string' is already registered.", ex.getMessage());
    }

}