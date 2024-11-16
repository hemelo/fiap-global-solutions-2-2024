package org.global.console.cli.commands;

import org.global.console.Main;
import org.global.console.dto.Sessao;
import org.global.console.dto.request.LoginDto;
import org.global.console.services.UsuarioService;
import org.global.console.utils.CommandUtils;
import org.global.console.utils.ConsoleUtils;
import org.jline.builtins.Completers;
import org.jline.reader.impl.completer.StringsCompleter;

import java.util.List;

public class LogoutCommand implements Command {

    @Override
    public void execute(String[] args) {

        if (!Main.isAuthenticated()) {
            ConsoleUtils.printStyledWarning("Você não está autenticado, não é possível realizar o logout.");
            return;
        }

        if (!CommandUtils.confirmOperation("Deseja realmente realizar o logout?")) {
            return;
        }

        Main.setSessao(null);
        ConsoleUtils.printStyledSuccess("Logout realizado com sucesso!");
    }

    @Override
    public String getDescription() {
        return "Responsável por realizar o logout do usuário";
    }

    @Override
    public String getSyntax() {
        return CommandUtils.createSyntax(getCommand(), null, null);
    }

    @Override
    public String getOptions() {
        return "";
    }

    @Override
    public List<String> getExamples() {
        return List.of("logout");
    }
}
