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

public class LoginCommand implements Command {
    @Override
    public void execute(String[] args) {

        if (Main.isAuthenticated()) {
            ConsoleUtils.printStyledWarning("Você já está autenticado, realize o logout para realizar o login com outra conta.");
            return;
        }

        if (args.length != 2) {
            CommandUtils.displayCommandHelp(this, true);
            return;
        }

        String user = args[0];
        String senha = args[1];

        UsuarioService usuarioService = UsuarioService.getInstance();

        LoginDto loginDto = new LoginDto(user, senha);
        Sessao sessao = null;

        try {
            sessao = usuarioService.login(loginDto);
        } catch (Exception e) {
            ConsoleUtils.printStyledError(e.getMessage());
            return;
        }

        if (sessao != null) {
            Main.setSessao(sessao);
            ConsoleUtils.printStyledSuccess("Login realizado com sucesso!");
            ConsoleUtils.printStyledSuccess("Bem-vindo, " + sessao.nome() + "!");
        }
    }

    @Override
    public String getDescription() {
        return "Responsável por realizar o login do usuário";
    }

    @Override
    public String getSyntax() {
        return CommandUtils.createSyntax(getCommand(), List.of("username", "senha"), null);
    }

    @Override
    public String getOptions() {
        return "";
    }

    @Override
    public List<String> getExamples() {
        return List.of("login john.doe 123456");
    }
}
