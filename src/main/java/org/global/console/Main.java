package org.global.console;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.global.console.cli.Introduction;
import org.global.console.cli.commands.Command;
import org.global.console.dto.Sessao;
import org.global.console.exceptions.AutenticacaoException;
import org.global.console.exceptions.SistemaException;
import org.global.console.threads.DataSourceCheckerThread;
import org.global.console.threads.DataSourceConnectionCheckerThread;
import org.global.console.utils.CommandUtils;
import org.global.console.utils.ConsoleUtils;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.widget.AutosuggestionWidgets;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class Main {
    private static Sessao sessao;
    private static Terminal terminal;
    private static LineReader reader;

    private static Map<String, Class<? extends Command>> commandMap;

    private static void init() throws Exception {

        DataSourceCheckerThread.initialize();
        DataSourceConnectionCheckerThread.initialize();

        commandMap = CommandUtils.loadCommands();

        terminal = TerminalBuilder.terminal();

        reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(ConsoleUtils.getCompleter())
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

            log.info("Comando recebido: {}", line);

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
                } catch (AutenticacaoException ex) {
                    ConsoleUtils.printStyledWarning(ex.getMessage());
                } catch (Exception ex) {
                    ConsoleUtils.printStyledError("Erro crítico ao executar comando: " + ex.getMessage());
                }

            } else {
                ConsoleUtils.printStyledError("Comando desconhecido: " + line);
                ConsoleUtils.printStyledError("Comandos disponíveis: " + CommandUtils.getAllCommandNames());
            }
        }
    }

    public static LineReader getReader() {

        if (reader == null) {
            throw new SistemaException("Reader não inicializado.");
        }

        return reader;
    }

    public static boolean isAuthenticated() {
        boolean result = sessao != null;
        log.debug(result ? "Usuário autenticado: {}" : "Usuário não autenticado.", Optional.ofNullable(sessao).map(Sessao::username).orElse(null));
        return result;
    }

    public static void setSessao(Sessao sessao) {

        log.info("Sessão definida: {}", sessao);
        Main.sessao = sessao;
    }
}