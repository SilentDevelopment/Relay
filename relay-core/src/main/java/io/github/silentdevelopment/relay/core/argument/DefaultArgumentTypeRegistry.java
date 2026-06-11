package io.github.silentdevelopment.relay.core.argument;

import io.github.silentdevelopment.relay.argument.ArgumentType;
import io.github.silentdevelopment.relay.argument.ArgumentTypeRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class DefaultArgumentTypeRegistry implements ArgumentTypeRegistry {

    private final Map<String, ArgumentType<?>> typesById;
    private final Map<Class<?>, List<ArgumentType<?>>> typesByClass;

    public DefaultArgumentTypeRegistry() {
        this.typesById = new LinkedHashMap<>();
        this.typesByClass = new LinkedHashMap<>();
    }

    public static DefaultArgumentTypeRegistry createDefault() {
        DefaultArgumentTypeRegistry registry = new DefaultArgumentTypeRegistry();
        registry.register(ArgumentTypes.STRING);
        registry.register(ArgumentTypes.GREEDY_STRING);
        registry.register(ArgumentTypes.BOOLEAN);
        registry.register(ArgumentTypes.INTEGER);
        registry.register(ArgumentTypes.LONG);
        registry.register(ArgumentTypes.DOUBLE);
        registry.register(ArgumentTypes.FLOAT);
        registry.register(ArgumentTypes.UUID);
        registry.register(ArgumentTypes.DURATION);
        registry.register(ArgumentTypes.BIG_DECIMAL);
        return registry;
    }

    @Override
    public <T> void register(ArgumentType<T> type) {
        Objects.requireNonNull(type, "type");

        String id = normalize(type.identifier());

        if (this.typesById.containsKey(id)) {
            throw new IllegalArgumentException("An argument type with id '" + type.identifier() + "' is already registered.");
        }

        this.typesById.put(id, type);
        this.typesByClass.computeIfAbsent(type.type(), key -> new ArrayList<>()).add(type);
    }

    @Override
    public Optional<ArgumentType<?>> find(String id) {
        Objects.requireNonNull(id, "id");
        return Optional.ofNullable(this.typesById.get(normalize(id)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<ArgumentType<T>> findDefault(Class<T> type) {
        Objects.requireNonNull(type, "type");

        List<ArgumentType<?>> types = this.typesByClass.get(type);

        if (types == null || types.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of((ArgumentType<T>) types.getFirst());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<ArgumentType<T>> findAll(Class<T> type) {
        Objects.requireNonNull(type, "type");

        List<ArgumentType<?>> types = this.typesByClass.get(type);

        if (types == null || types.isEmpty()) {
            return List.of();
        }

        return (List<ArgumentType<T>>) (List<?>) List.copyOf(types);
    }

    @Override
    public Collection<ArgumentType<?>> types() {
        return List.copyOf(this.typesById.values());
    }

    private static String normalize(String id) {
        if (id.isBlank()) {
            throw new IllegalArgumentException("id cannot be blank.");
        }

        return id.toLowerCase(Locale.ROOT);
    }

}