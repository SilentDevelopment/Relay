package io.github.silentdevelopment.relay.core.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;

import java.util.Locale;
import java.util.Objects;

public final class EnumParser<E extends Enum<E>> implements ArgumentParser<E> {

    private final Class<E> enumType;

    public EnumParser(Class<E> enumType) {
        this.enumType = Objects.requireNonNull(enumType, "enumType");
    }

    @Override
    public ParseResult<E> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a value.");
        }

        String input = reader.read();

        for (E constant : this.enumType.getEnumConstants()) {
            if (matches(constant, input)) {
                return ParseResult.success(constant);
            }
        }

        return ParseResult.failure("Invalid " + this.enumType.getSimpleName().toLowerCase(Locale.ROOT) + ": " + input);
    }

    private boolean matches(E constant, String input) {
        String name = constant.name();
        return name.equalsIgnoreCase(input) || name.replace('_', '-').equalsIgnoreCase(input);
    }

}