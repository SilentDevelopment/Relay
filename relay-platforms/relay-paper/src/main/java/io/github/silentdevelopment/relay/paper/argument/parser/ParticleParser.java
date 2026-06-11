package io.github.silentdevelopment.relay.paper.argument.parser;

import io.github.silentdevelopment.relay.argument.parser.ArgumentParser;
import io.github.silentdevelopment.relay.argument.parser.ParseResult;
import io.github.silentdevelopment.relay.argument.reader.ArgumentReader;
import org.bukkit.Particle;

import java.util.Locale;

public final class ParticleParser implements ArgumentParser<Particle> {

    @Override
    public ParseResult<Particle> parse(ArgumentReader reader) {
        if (!reader.hasNext()) {
            return ParseResult.failure("Expected a particle.");
        }

        String value = normalize(reader.read());

        for (Particle particle : Particle.values()) {
            if (particle.name().equalsIgnoreCase(value)) {
                return ParseResult.success(particle);
            }
        }

        return ParseResult.failure("Unknown particle: " + value);
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT).replace(' ', '_').replace('-', '_');
    }

}