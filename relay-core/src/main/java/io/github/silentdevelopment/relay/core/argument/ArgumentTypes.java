package io.github.silentdevelopment.relay.core.argument;

import io.github.silentdevelopment.relay.argument.ArgumentType;
import io.github.silentdevelopment.relay.core.argument.parser.BigDecimalParser;
import io.github.silentdevelopment.relay.core.argument.parser.BooleanParser;
import io.github.silentdevelopment.relay.core.argument.parser.DoubleParser;
import io.github.silentdevelopment.relay.core.argument.parser.DurationParser;
import io.github.silentdevelopment.relay.core.argument.parser.EnumParser;
import io.github.silentdevelopment.relay.core.argument.parser.FloatParser;
import io.github.silentdevelopment.relay.core.argument.parser.GreedyStringParser;
import io.github.silentdevelopment.relay.core.argument.parser.IntegerParser;
import io.github.silentdevelopment.relay.core.argument.parser.LongParser;
import io.github.silentdevelopment.relay.core.argument.parser.StringParser;
import io.github.silentdevelopment.relay.core.argument.parser.UUIDParser;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Locale;
import java.util.UUID;

public final class ArgumentTypes {

    private ArgumentTypes() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static final ArgumentType<String> STRING = new DefaultArgumentType<>("string", String.class, new StringParser());
    public static final ArgumentType<String> GREEDY_STRING = new DefaultArgumentType<>("greedy_string", String.class, new GreedyStringParser());
    public static final ArgumentType<Boolean> BOOLEAN = new DefaultArgumentType<>("boolean", Boolean.class, new BooleanParser());
    public static final ArgumentType<Integer> INTEGER = new DefaultArgumentType<>("integer", Integer.class, new IntegerParser());
    public static final ArgumentType<Long> LONG = new DefaultArgumentType<>("long", Long.class, new LongParser());
    public static final ArgumentType<Double> DOUBLE = new DefaultArgumentType<>("double", Double.class, new DoubleParser());
    public static final ArgumentType<Float> FLOAT = new DefaultArgumentType<>("float", Float.class, new FloatParser());
    public static final ArgumentType<UUID> UUID = new DefaultArgumentType<>("uuid", UUID.class, new UUIDParser());
    public static final ArgumentType<Duration> DURATION = new DefaultArgumentType<>("duration", Duration.class, new DurationParser());
    public static final ArgumentType<BigDecimal> BIG_DECIMAL = new DefaultArgumentType<>("big_decimal", BigDecimal.class, new BigDecimalParser());

    public static <E extends Enum<E>> ArgumentType<E> enumType(Class<E> enumType) {
        if (enumType == null) {
            throw new IllegalArgumentException("enumType cannot be null.");
        }

        return new DefaultArgumentType<>(
                "enum:" + enumType.getSimpleName().toLowerCase(Locale.ROOT),
                enumType,
                new EnumParser<>(enumType)
        );
    }

}