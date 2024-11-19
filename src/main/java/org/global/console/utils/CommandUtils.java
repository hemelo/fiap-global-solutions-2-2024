package org.global.console.utils;

import jakarta.validation.ConstraintViolation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.global.console.Main;
import org.global.console.cli.commands.*;
import org.global.console.exceptions.AutenticacaoException;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe utilitária para manipulação de comandos.
 */
public class CommandUtils {

    private static final String COMMAND_PACKAGE = "org.global.console.cli.commands";
    private static Set<Class<? extends Command>> commands;

    public static Set<Class<? extends Command>> getAllCommands() {

        if (commands != null) {
            return commands;
        }

        Reflections reflections = new Reflections(COMMAND_PACKAGE);
        Set<Class<? extends Command>>  _commands = reflections.getSubTypesOf(Command.class);
        LinkedHashSet<Class<? extends Command>>__commands = _commands.stream().sorted(Comparator.comparing(CommandUtils::getCommandName)).collect(Collectors.toCollection(LinkedHashSet::new));

        List<Class<? extends Command>> lastCommands = List.of(RegisterCommand.class, LoginCommand.class, LogoutCommand.class, HelpCommand.class, SysInfoCommand.class);

        for (Class<? extends Command> command : lastCommands) {
            __commands.remove(command);
            __commands.addLast(command);
        }

        CommandUtils.commands = __commands;
        return commands;
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
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new IllegalArgumentException("Error creating command instance");
        }
    }

    public static String getCommandName(Class<? extends Command> command) {
        return command.getSimpleName().replace("Command", "").toLowerCase();
    }

    public static Pair<Set<Command>, Set<Class<? extends Command>>> getAllCommandsInstances() {
        Set<Class<? extends Command>> failedCommands = new HashSet<>();
        Set<Class<? extends Command>> commands = getAllCommands();
        Set<Command> commandInstances = new LinkedHashSet<>();

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
     *
     * @param command         Nome do comando
     * @param requiredOptions Opções obrigatórias
     * @param options         Opções
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

    public static void displayCommandHelp(Command command, boolean showAlerta) {

        if (showAlerta) {
            ConsoleUtils.printStyledWarning("Parece que o comando está faltando informações, segue a ajuda para o comando: ");
        }

        StringBuilder sb = new StringBuilder();

        sb.append(new AttributedString(
                "Comando: ",
                AttributedStyle.DEFAULT.foreground(CommandUtils.ColorExtensions.GRAY)).toAnsi());

        sb.append(new AttributedString(
                command.getCommand(),
                AttributedStyle.BOLD).toAnsi());

        sb.append(new AttributedString(
                "\nDescrição: ",
                AttributedStyle.DEFAULT.foreground(CommandUtils.ColorExtensions.GRAY)).toAnsi());

        sb.append(new AttributedString(
                command.getDescription(),
                AttributedStyle.DEFAULT).toAnsi());

        sb.append(new AttributedString(
                "\nSintaxe: ",
                AttributedStyle.DEFAULT.foreground(CommandUtils.ColorExtensions.GRAY)).toAnsi());

        sb.append(new AttributedString(
                command.getSyntax(),
                AttributedStyle.DEFAULT).toAnsi());

        sb.append(new AttributedString(
                "\nExemplos: ",
                AttributedStyle.DEFAULT.foreground(CommandUtils.ColorExtensions.GRAY)).toAnsi());

        for (String example : command.getExamples()) {
            sb.append(new AttributedString(
                    "\n  " + example,
                    AttributedStyle.DEFAULT).toAnsi());
        }

        ConsoleUtils.printWithTypingEffect(sb.toString());
    }

    public static void printDetail(String label, String value) {
        ConsoleUtils.printWithTypingEffect(new AttributedString(label + " : ",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).toAnsi() +
                new AttributedString(value,
                        AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).toAnsi());
    }

    public static boolean confirmOperation(String s) {

        ConsoleUtils.printWithTypingEffect(new AttributedString(s + " (s/n): ",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).toAnsi());

        Boolean result = ConsoleUtils.readBoolean();

        if (!result) {
            ConsoleUtils.printStyledWarning("Operação cancelada.");
        }

        return result;
    }

    public static Map<String, Class<? extends Command>> loadCommands() {

        Set<Class<? extends Command>> commandClasses = CommandUtils.getAllCommands();

        Map<String, Class<? extends Command>> commandMap = new HashMap<>();

        for (Class<? extends Command> commandClass : commandClasses) {
            String commandName = CommandUtils.getCommandName(commandClass);
            commandMap.put(commandName, commandClass);
        }

        return commandMap;
    }

    public static <T> void printViolations(Set<ConstraintViolation<T>> violations, String message) {

        ConsoleUtils.printStyledError("Erro ao validar dados:");

        for (ConstraintViolation<T> violation : violations) {
            ConsoleUtils.printStyledError(" - " + violation.getMessage());
        }

        ConsoleUtils.printStyledWarning("Dados inválidos. " + message);
    }

    public static void loginCheck() {
        if (!Main.isAuthenticated()) {
            throw new AutenticacaoException("Você precisa estar autenticado para executar este comando.");
        }
    }

    public static String getAllCommandNames() {
        return getAllCommands().stream().map(CommandUtils::getCommandName).collect(Collectors.joining(", "));
    }

    /**
     * Extensões de cores para comandos.
     */
    public static final class ColorExtensions {
        public static final int GRAY = 244;
    }
}
