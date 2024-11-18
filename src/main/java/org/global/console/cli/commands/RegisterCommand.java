package org.global.console.cli.commands;

import jakarta.validation.ConstraintViolation;
import org.apache.commons.lang3.tuple.Pair;
import org.global.console.Main;
import org.global.console.dto.request.create.CreateUserDto;
import org.global.console.services.UsuarioService;
import org.global.console.utils.CommandUtils;
import org.global.console.utils.ConsoleUtils;
import org.global.console.utils.ValidationUtils;
import org.jline.reader.LineReader;

import java.util.*;

public class RegisterCommand implements Command {
    @Override
    public void execute(String[] args) {

        if (Main.isAuthenticated()) {
            ConsoleUtils.printStyledWarning("Você já está autenticado, realize o logout para realizar o login com outra conta.");
            return;
        }

        UsuarioService usuarioService = UsuarioService.getInstance();
        LineReader reader = Main.getReader();

        String login = reader.readLine("Login: ");
        String nome = reader.readLine("Nome: ");
        String email = reader.readLine("Email: ");
        String senha = reader.readLine("Senha: ");

        CreateUserDto createUsuarioDto = new CreateUserDto(login, nome, email, senha);

        Set<ConstraintViolation<CreateUserDto>> violations = ValidationUtils.validate(createUsuarioDto);

        if (!Objects.requireNonNullElse(violations, new HashSet<>()).isEmpty()) {
            CommandUtils.printViolations(violations, "Tente novamente.");
            return;
        }

        try {
            usuarioService.createUser(createUsuarioDto);
            ConsoleUtils.printStyledSuccess("Registro realizado com sucesso.");
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao registrar usuário");
        }
    }

    @Override
    public String getDescription() {
        return "Registrar-se no sistema.";
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
