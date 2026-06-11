package io.github.silentdevelopment.relay.paper.argument;

import io.github.silentdevelopment.relay.argument.ArgumentType;
import io.github.silentdevelopment.relay.argument.ArgumentTypeRegistry;
import io.github.silentdevelopment.relay.core.argument.DefaultArgumentTypeRegistry;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PaperArgumentTypeRegistry implements ArgumentTypeRegistry {

    private final ArgumentTypeRegistry delegate;

    public PaperArgumentTypeRegistry() {
        this.delegate = new DefaultArgumentTypeRegistry();
    }

    private PaperArgumentTypeRegistry(ArgumentTypeRegistry delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    public static PaperArgumentTypeRegistry createDefault() {
        PaperArgumentTypeRegistry registry = new PaperArgumentTypeRegistry(DefaultArgumentTypeRegistry.createDefault());
        PaperArgumentTypes.registerDefaults(registry);
        return registry;
    }

    @Override
    public <T> void register(ArgumentType<T> type) {
        this.delegate.register(type);
    }

    @Override
    public Optional<ArgumentType<?>> find(String id) {
        return this.delegate.find(id);
    }

    @Override
    public <T> Optional<ArgumentType<T>> findDefault(Class<T> type) {
        return this.delegate.findDefault(type);
    }

    @Override
    public <T> List<ArgumentType<T>> findAll(Class<T> type) {
        return this.delegate.findAll(type);
    }

    @Override
    public Collection<ArgumentType<?>> types() {
        return this.delegate.types();
    }

}