package io.github.silentdevelopment.relay.paper.command.builder;

import io.github.silentdevelopment.relay.command.Command;
import io.github.silentdevelopment.relay.core.requirement.DefaultCommandAccessResolver;
import io.github.silentdevelopment.relay.requirement.RequirementResult;
import io.github.silentdevelopment.relay.text.CommandText;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaperCommandBuilderTest {

    @Test
    void permissionUsesCustomDenialTextAndAddsPermissionPlaceholder() {
        CommandText denial = CommandText.keyed(
                "test.no-permission",
                "Missing {permission}.",
                Map.of("extra", "value")
        );

        Command command = PaperCommandBuilder.literal("secure")
                .permission("relay.test.secure", denial)
                .noArgs()
                .build();

        RequirementResult result = evaluate(command, sender("Tester", Set.of()));

        assertTrue(result.isDenied());
        assertEquals("test.no-permission", result.getText().orElseThrow().key());
        assertEquals("Missing {permission}.", result.getText().orElseThrow().fallback());
        assertEquals(Map.of(
                "extra", "value",
                "permission", "relay.test.secure"
        ), result.getText().orElseThrow().placeholders());
    }

    @Test
    void permissionAllowsSenderWithPermission() {
        Command command = PaperCommandBuilder.literal("secure")
                .permission("relay.test.secure")
                .noArgs()
                .build();

        RequirementResult result = evaluate(command, sender("Tester", Set.of("relay.test.secure")));

        assertTrue(result.isAllowed());
    }

    @Test
    void anyPermissionAllowsWhenAnyPermissionMatches() {
        Command command = PaperCommandBuilder.literal("secure")
                .anyPermission("relay.test.one", "relay.test.two")
                .noArgs()
                .build();

        RequirementResult result = evaluate(command, sender("Tester", Set.of("relay.test.two")));

        assertTrue(result.isAllowed());
    }

    @Test
    void anyPermissionDeniesWhenNoPermissionMatches() {
        CommandText denial = CommandText.keyed(
                "test.any-permission",
                "Missing one of {permissions}.",
                Map.of()
        );

        Command command = PaperCommandBuilder.literal("secure")
                .anyPermission(denial, "relay.test.one", "relay.test.two")
                .noArgs()
                .build();

        RequirementResult result = evaluate(command, sender("Tester", Set.of()));

        assertTrue(result.isDenied());
        assertEquals("test.any-permission", result.getText().orElseThrow().key());
        assertEquals(Map.of("permissions", "relay.test.one, relay.test.two"), result.getText().orElseThrow().placeholders());
    }

    @Test
    void allPermissionsAllowsWhenAllPermissionsMatch() {
        Command command = PaperCommandBuilder.literal("secure")
                .allPermissions("relay.test.one", "relay.test.two")
                .noArgs()
                .build();

        RequirementResult result = evaluate(command, sender("Tester", Set.of("relay.test.one", "relay.test.two")));

        assertTrue(result.isAllowed());
    }

    @Test
    void allPermissionsDeniesWhenAnyPermissionIsMissing() {
        CommandText denial = CommandText.keyed(
                "test.all-permissions",
                "Missing all of {permissions}.",
                Map.of()
        );

        Command command = PaperCommandBuilder.literal("secure")
                .allPermissions(denial, "relay.test.one", "relay.test.two")
                .noArgs()
                .build();

        RequirementResult result = evaluate(command, sender("Tester", Set.of("relay.test.one")));

        assertTrue(result.isDenied());
        assertEquals("test.all-permissions", result.getText().orElseThrow().key());
        assertEquals(Map.of("permissions", "relay.test.one, relay.test.two"), result.getText().orElseThrow().placeholders());
    }

    @Test
    void playerOnlyAllowsPlayerSources() {
        Command command = PaperCommandBuilder.literal("player")
                .playerOnly()
                .noArgs()
                .build();

        RequirementResult result = evaluate(command, player("Tester", Set.of()));

        assertTrue(result.isAllowed());
    }

    @Test
    void playerOnlyDeniesNonPlayerSources() {
        Command command = PaperCommandBuilder.literal("player")
                .playerOnly(CommandText.of("test.player-only", "Players only."))
                .noArgs()
                .build();

        RequirementResult result = evaluate(command, sender("Console", Set.of()));

        assertTrue(result.isDenied());
        assertEquals("test.player-only", result.getText().orElseThrow().key());
        assertEquals("Players only.", result.getText().orElseThrow().fallback());
    }

    @Test
    void consoleOnlyAllowsNonPlayerSources() {
        Command command = PaperCommandBuilder.literal("console")
                .consoleOnly()
                .noArgs()
                .build();

        RequirementResult result = evaluate(command, sender("Console", Set.of()));

        assertTrue(result.isAllowed());
    }

    @Test
    void consoleOnlyDeniesPlayerSources() {
        Command command = PaperCommandBuilder.literal("console")
                .consoleOnly(CommandText.of("test.console-only", "Console only."))
                .noArgs()
                .build();

        RequirementResult result = evaluate(command, player("Tester", Set.of()));

        assertTrue(result.isDenied());
        assertEquals("test.console-only", result.getText().orElseThrow().key());
        assertEquals("Console only.", result.getText().orElseThrow().fallback());
    }

    @Test
    void rejectsBlankPermission() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> PaperCommandBuilder.literal("secure")
                .permission(" ")
                .noArgs()
                .build());

        assertEquals("permission cannot be null or blank.", exception.getMessage());
    }

    @Test
    void rejectsEmptyAnyPermissionList() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> PaperCommandBuilder.literal("secure")
                .anyPermission()
                .noArgs()
                .build());

        assertEquals("permissions cannot be null or empty.", exception.getMessage());
    }

    @Test
    void asCommandBuilderExposesDelegateForCommandGroups() {
        Command command = PaperCommandBuilder.literal("group")
                .description("Group command.")
                .permission("relay.test.group")
                .asCommandBuilder()
                .subcommand(PaperCommandBuilder.literal("child")
                        .noArgs()
                        .build())
                .build();

        assertEquals("group", command.name());
        assertEquals(1, command.subCommands().size());
        assertEquals("child", command.subCommands().getFirst().name());
    }

    private static RequirementResult evaluate(Command command, CommandSender sender) {
        return new DefaultCommandAccessResolver<CommandSender>().evaluate(sender, command);
    }

    private static CommandSender sender(String name, Set<String> permissions) {
        return proxy(CommandSender.class, name, permissions);
    }

    private static Player player(String name, Set<String> permissions) {
        return proxy(Player.class, name, permissions);
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, String name, Set<String> permissions) {
        InvocationHandler handler = (proxy, method, args) -> {
            if ((method.getName().equals("name") || method.getName().equals("getName")) && method.getParameterCount() == 0) {
                return name;
            }

            if (method.getName().equals("hasPermission") && args != null && args.length == 1 && args[0] instanceof String permission) {
                return permissions.contains(permission);
            }

            if (method.getName().equals("toString") && method.getParameterCount() == 0) {
                return name;
            }

            return defaultValue(method.getReturnType());
        };

        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
    }

    private static Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }

        if (type == boolean.class) {
            return false;
        }

        if (type == byte.class) {
            return (byte) 0;
        }

        if (type == short.class) {
            return (short) 0;
        }

        if (type == int.class) {
            return 0;
        }

        if (type == long.class) {
            return 0L;
        }

        if (type == float.class) {
            return 0F;
        }

        if (type == double.class) {
            return 0D;
        }

        if (type == char.class) {
            return '\0';
        }

        throw new IllegalStateException("Unsupported primitive type: " + type.getName());
    }

}