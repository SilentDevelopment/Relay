package io.github.silentdevelopment.relay.paper.support;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public final class PaperProxyFactory {

    private PaperProxyFactory() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static CommandSender sender(String name) {
        InvocationHandler handler = (proxy, method, args) -> {
            if (method.getName().equals("name") || method.getName().equals("getName")) {
                return name;
            }

            return defaultValue(method.getReturnType());
        };

        return (CommandSender) Proxy.newProxyInstance(CommandSender.class.getClassLoader(), new Class<?>[]{CommandSender.class}, handler);
    }

    public static CommandSourceStack sourceStack(CommandSender sender) {
        InvocationHandler handler = (proxy, method, args) -> {
            if (method.getName().equals("getSender")) {
                return sender;
            }

            if (method.getName().equals("withExecutor") || method.getName().equals("withLocation")) {
                return proxy;
            }

            return defaultValue(method.getReturnType());
        };

        return (CommandSourceStack) Proxy.newProxyInstance(CommandSourceStack.class.getClassLoader(), new Class<?>[]{CommandSourceStack.class}, handler);
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
