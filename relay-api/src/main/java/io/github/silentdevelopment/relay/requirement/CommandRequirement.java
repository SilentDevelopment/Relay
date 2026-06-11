package io.github.silentdevelopment.relay.requirement;

public interface CommandRequirement<S> {

    RequirementResult test(S source);

}