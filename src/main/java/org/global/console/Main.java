package org.global.console;

import org.apache.commons.lang3.ArrayUtils;
import org.global.console.cli.Introduction;
import org.global.console.cli.commands.Command;
import org.global.console.utils.CommandUtils;
import org.global.console.utils.ConsoleUtils;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.widget.AutosuggestionWidgets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {

    private static Terminal terminal;
    private static LineReader reader;
    private static Map<String, Class<? extends Command>> commandMap;

    private static void init() throws Exception {
        registerCommands();

        terminal = TerminalBuilder.terminal();

        reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter("help", "exit"))
                .build();

        //new SkipWidget(reader);

        // Create autosuggestion widgets
        AutosuggestionWidgets autosuggestionWidgets = new AutosuggestionWidgets(reader);

        // Enable autosuggestions
        autosuggestionWidgets.enable();

        Introduction.display();
    }

    public static void main(String[] args) throws Exception {

        init();

        String line;
        String[] tokens;
        Command commandInstance;
        Class<? extends Command> command;

        while ((line = reader.readLine("> ")) != null) {

            tokens = line.split(" ");

            if (tokens.length == 0) {
                continue;
            }

            if ("exit".equals(tokens[0])) {
                ConsoleUtils.printWithTypingEffect("Fechando a aplicação.");
                break;
            }

            command = commandMap.get(tokens[0]);

            if (command != null) {

                try {
                    commandInstance = command.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    ConsoleUtils.printWithTypingEffect("Erro crítico ao iniciar comando: " + e.getMessage());
                    continue;
                }

                tokens = ArrayUtils.remove(tokens, 0);

                try {
                    commandInstance.execute(tokens);
                } catch (Exception ex) {
                    ConsoleUtils.printStyledError("Erro crítico ao executar comando: " + ex.getMessage());
                }

            } else {
                ConsoleUtils.printStyledError("Comando desconhecido: " + line);
                ConsoleUtils.printStyledError("Comandos disponíveis: " + commandMap.keySet());
            }
        }
    }

    private static void registerCommands() throws Exception {
        Set<Class<? extends Command>> commandClasses = CommandUtils.getAllCommands();

        commandMap = new HashMap<>();

        for (Class<? extends Command> commandClass : commandClasses) {
            String commandName = CommandUtils.getCommandName(commandClass);
            commandMap.put(commandName, commandClass);
        }
    }

}