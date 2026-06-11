package io.github.silentdevelopment.relay.core.requirement;

import io.github.silentdevelopment.relay.requirement.CommandRequirement;
import io.github.silentdevelopment.relay.requirement.RequirementResult;

import java.util.Objects;
import java.util.function.Predicate;

public final class PredicateCommandRequirement<S> implements CommandRequirement<S> {

    private final Predicate<S> predicate;
    private final String message;

    public PredicateCommandRequirement(Predicate<S> predicate, String message) {
        this.predicate = Objects.requireNonNull(predicate, "predicate");

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message cannot be null or blank.");
        }

        this.message = message;
    }

    @Override
    public RequirementResult test(S source) {
        return this.predicate.test(source) ? RequirementResult.allow() : RequirementResult.deny(this.message);
    }

}