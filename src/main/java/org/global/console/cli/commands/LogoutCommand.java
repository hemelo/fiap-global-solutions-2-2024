package org.global.console.cli.commands;

import org.global.console.Main;
import org.global.console.utils.CommandUtils;
import org.global.console.utils.ConsoleUtils;

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
    public List<String> getExamples() {
        return List.of(getCommand());
    }
}
