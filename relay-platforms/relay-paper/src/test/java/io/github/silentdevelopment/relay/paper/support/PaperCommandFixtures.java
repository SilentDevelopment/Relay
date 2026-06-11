package io.github.silentdevelopment.relay.paper.support;

import io.github.silentdevelopment.relay.argument.Argument;
import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.core.argument.ArgumentTypes;
import io.github.silentdevelopment.relay.core.command.builder.CommandBuilder;
import io.github.silentdevelopment.relay.core.requirement.PredicateCommandRequirement;
import io.github.silentdevelopment.relay.paper.PaperCommandManager;
import io.github.silentdevelopment.relay.paper.command.PaperCommandHandler;
import io.github.silentdevelopment.relay.paper.argument.PaperArgumentTypeRegistry;
import org.bukkit.command.CommandSender;

public final class PaperCommandFixtures {

    private PaperCommandFixtures() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static final Argument<String> MESSAGE = Argument.greedyRequired("message", ArgumentTypes.GREEDY_STRING);

    public static PaperCommandManager manager(CapturingResponseFunction responseFunction) {
        return new PaperCommandManager(builder -> builder.argumentTypeRegistry(new PaperArgumentTypeRegistry()).responseFunction(responseFunction));
    }

    public static Command noArgsCommand(String name) {
        return CommandBuilder.<CommandSender>literal(name)
                .description("No-args command.")
                .noArgs()
                .build();
    }

    public static Command requiredMessageCommand(String name) {
        return CommandBuilder.<CommandSender>literal(name)
                .description("Command with a required argument.")
                .signature(MESSAGE)
                .build();
    }

    public static Command restrictedCommand(String name) {
        return CommandBuilder.<CommandSender>literal(name)
                .description("Restricted command.")
                .noArgs()
                .requirement(new PredicateCommandRequirement<>(sender -> false, "Forbidden."))
                .build();
    }

    public static void register(PaperCommandManager manager, Command root, PaperCommandHandler handler) {
        manager.registerRoot(root);
        manager.bind(root, handler);
    }

}
