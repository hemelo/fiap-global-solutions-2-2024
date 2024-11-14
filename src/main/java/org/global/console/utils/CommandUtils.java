package org.global.console.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.global.console.cli.commands.Command;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe utilitária para manipulação de comandos.
 */
public class CommandUtils {

    public static Set<Class<? extends Command>>  getAllCommands() {
        Reflections reflections = new Reflections("org.global.console.cli.commands");
        return reflections.getSubTypesOf(Command.class);
    }

    public static Class<? extends Command> getCommandClass(String commandName) {
        Set<Class<? extends Command>> commands = getAllCommands();

        for (Class<? extends Command> command : commands) {
            if (getCommandName(command).equalsIgnoreCase(commandName)) {
                return command;
            }
        }

        throw new IllegalArgumentException("Command not found");
    }

    public static Command getCommandInstance(String commandName) {
        Class<? extends Command> command = getCommandClass(commandName);
        return getCommandInstance(command);
    }

    public static Command getCommandInstance(Class<? extends Command> command) throws IllegalArgumentException {
        try {
            return command.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalArgumentException("Error creating command instance");
        }
    }

    public static String getCommandName(Class<? extends Command> command) {
        return command.getSimpleName().replace("Command", "").toLowerCase();
    }

    public static Pair<Set<Command>, Set<Class<? extends Command>>> getAllCommandsInstances() {
        Set<Class<? extends Command>> failedCommands = new HashSet<>();
        Set<Class<? extends Command>> commands = getAllCommands();
        Set<Command> commandInstances = new HashSet<>();

        for (Class<? extends Command> command : commands) {
            try {
                commandInstances.add(getCommandInstance(command));
            } catch (IllegalArgumentException e) {
                failedCommands.add(command);
            }
        }

        return Pair.of(commandInstances, failedCommands);
    }

    /**
     * Cria a sintaxe de um comando.
     * @param command Nome do comando
     * @param requiredOptions Opções obrigatórias
     * @param options Opções
     * @return Sintaxe do comando
     */
    public static String createSyntax(String command, List<String> requiredOptions, List<String> options) {
        StringBuilder syntax = new StringBuilder(command);

        if (CollectionUtils.isNotEmpty(requiredOptions)) {
            for (String requiredOption : requiredOptions) {
                syntax.append(new AttributedString(" <" + requiredOption + ">", AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)).toAnsi());
            }
        }

        if (CollectionUtils.isNotEmpty(options)) {
            for (String option : options) {
                syntax.append(new AttributedString(" [" + option + "]", AttributedStyle.DEFAULT.foreground(ColorExtensions.GRAY)).toAnsi());
            }
        }

        return syntax.toString();
    }

    /**
     * Extensões de cores para comandos.
     */
    public static final class ColorExtensions {
        public static final int GRAY = 244;
    }
}
