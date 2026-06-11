package io.github.silentdevelopment.relay.argument;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ArgumentTypeRegistry {

    <T> void register(ArgumentType<T> type);

    Optional<ArgumentType<?>> find(String id);

    <T> Optional<ArgumentType<T>> findDefault(Class<T> type);

    <T> List<ArgumentType<T>> findAll(Class<T> type);

    Collection<ArgumentType<?>> types();

}