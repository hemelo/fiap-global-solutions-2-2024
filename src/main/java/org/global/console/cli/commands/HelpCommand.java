package org.global.console.cli.commands;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.global.console.utils.CommandUtils;
import org.global.console.utils.ConsoleUtils;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Comando de ajuda.
 * Exibe a ajuda para um comando específico ou para todos os comandos disponíveis.
 */
public class HelpCommand implements Command {
    @Override
    public void execute(String[] args) {

        if (args.length == 0) {
            helpGlobal();
        }

        if (args.length == 1) {
            helpCommand(args[0]);
        }
    }

    public void helpGlobal() {

        Command commandInstance;
        Pair<Set<Command>, Set<Class<? extends Command>>> commandInstancesResult;

        try {
            commandInstancesResult = CommandUtils.getAllCommandsInstances();

            if (CollectionUtils.isEmpty(commandInstancesResult.getLeft())) {
                ConsoleUtils.printStyledError("Nenhum comando encontrado.");
                return;
            }

        } catch (Exception ex) {
            ConsoleUtils.printStyledError("Erro ao buscar lista de comandos.");
            return;
        }

        for (Command command : commandInstancesResult.getLeft()) {
            displayCommandHelp(command);
        }

        if (CollectionUtils.isNotEmpty(commandInstancesResult.getRight())) {
            ConsoleUtils.printStyledWarning("Erro ao buscar ajuda para os seguintes comandos: " + commandInstancesResult.getRight().stream().map(CommandUtils::getCommandName).collect(Collectors.joining()));
        }
    }

    public void helpCommand(String commandName) {

        Command commandInstance;

        try {
            commandInstance = CommandUtils.getCommandInstance(commandName);
        } catch (Exception ex) {
            ConsoleUtils.printStyledError("Erro ao buscar comando.");
            return;
        }

        displayDetailedCommandHelp(commandInstance);
    }

    private void displayCommandHelp(Command command) {

        StringBuilder sb = new StringBuilder();

        sb.append(new AttributedString(
                "'",
                AttributedStyle.DEFAULT.foreground(CommandUtils.ColorExtensions.GRAY)).toAnsi());

        sb.append(new AttributedString(
                command.getCommand(),
                AttributedStyle.BOLD.foreground(AttributedStyle.CYAN)).toAnsi());

        sb.append(new AttributedString(
                "' - ",
                AttributedStyle.DEFAULT.foreground(CommandUtils.ColorExtensions.GRAY)).toAnsi());

        sb.append(new AttributedString(
                command.getDescription(),
                AttributedStyle.DEFAULT).toAnsi());

        ConsoleUtils.printWithTypingEffect(sb.toString());
    }

    private void displayDetailedCommandHelp(Command command) {

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
                    "\nOpções: ",
                    AttributedStyle.DEFAULT.foreground(CommandUtils.ColorExtensions.GRAY)).toAnsi());

            sb.append(new AttributedString(
                    command.getOptions(),
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

    @Override
    public String getDescription() {
        return "Exibe a ajuda para um comando específico ou para todos os comandos disponíveis.";
    }

    @Override
    public String getSyntax() {
        return CommandUtils.createSyntax(getCommand(), null, List.of("comando"));
    }

    @Override
    public String getOptions() {
        return "comando - O nome do comando para o qual você deseja obter ajuda.";
    }

    @Override
    public List<String> getExamples() {
        return List.of(
                getCommand(),
                getCommand() + " sysinfo"
        );
    }
}
