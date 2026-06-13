package io.github.silentdevelopment.relay.core.requirement;

import io.github.silentdevelopment.relay.requirement.CommandRequirement;
import io.github.silentdevelopment.relay.requirement.RequirementResult;
import io.github.silentdevelopment.relay.text.CommandText;

import java.util.Objects;
import java.util.function.Predicate;

public final class PredicateCommandRequirement<S> implements CommandRequirement<S> {

    private final Predicate<S> predicate;
    private final CommandText denial;

    public PredicateCommandRequirement(Predicate<S> predicate, String message) {
        this(predicate, CommandText.of(message));
    }

    public PredicateCommandRequirement(Predicate<S> predicate, CommandText denial) {
        this.predicate = Objects.requireNonNull(predicate, "predicate");
        this.denial = Objects.requireNonNull(denial, "denial");
    }

    @Override
    public RequirementResult test(S source) {
        if (this.predicate.test(source)) {
            return RequirementResult.allow();
        }

        return RequirementResult.deny(this.denial);
    }
}