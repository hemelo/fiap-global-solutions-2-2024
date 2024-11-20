package org.global.console.cli.commands;

import jakarta.validation.ConstraintViolation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.global.console.Main;
import org.global.console.dto.request.create.CreateUserDto;
import org.global.console.dto.request.update.UpdateUserDto;
import org.global.console.dto.response.UsuarioResponse;
import org.global.console.exceptions.RecursoNaoEncontradoException;
import org.global.console.services.UsuarioService;
import org.global.console.utils.CommandUtils;
import org.global.console.utils.ConsoleUtils;
import org.global.console.utils.FormatUtils;
import org.global.console.utils.ValidationUtils;
import org.jline.builtins.Completers;
import org.jline.reader.LineReader;
import org.jline.reader.impl.completer.StringsCompleter;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UsuarioCommand implements Command {

    @Override
    public void execute(String[] args) {

        if (args.length == 0) {
            CommandUtils.displayCommandHelp(this, true);
            return;
        }

        String[] subArgs = ArrayUtils.remove(args, 0);

        switch (args[0]) {
            case "listar":
                listar(subArgs);
                break;
            case "adicionar":
                CommandUtils.loginCheck();
                adicionar(subArgs);
                break;
            case "remover":
                CommandUtils.loginCheck();
                remover(subArgs);
                break;
            case "atualizar":
                CommandUtils.loginCheck();
                atualizar(subArgs);
                break;
            case "detalhar":
                detalhar(subArgs);
                break;
            default:
                CommandUtils.displayCommandHelp(this, true);
        }

    }

    private void detalhar(String[] subArgs) {

        if (subArgs.length != 1) {
            CommandUtils.displayCommandHelp(this, true);
            return;
        }

        String login = subArgs[0];

        if (StringUtils.isBlank(login)) {
            ConsoleUtils.printStyledError("Login não pode ser vazio.");
            return;
        }

        UsuarioResponse usuarioResponse;

        try {
            usuarioResponse = UsuarioService.getInstance().viewUsuario(login);
        } catch (RecursoNaoEncontradoException ex) {
            ConsoleUtils.printStyledError("Usuario não encontrado.");
            return;
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao buscar usuario");
            return;
        }

        if (usuarioResponse == null) {
            ConsoleUtils.printStyledError("Usuario não encontrado.");
            return;
        }

        ConsoleUtils.printWithTypingEffect("Detalhes do usuario:");

        CommandUtils.printDetail("Login", usuarioResponse.login());
        CommandUtils.printDetail("Nome", usuarioResponse.nome());
        CommandUtils.printDetail("Email", usuarioResponse.email());
        CommandUtils.printDetail("Data de Criação", FormatUtils.formatDateTime(usuarioResponse.createdAt()));
        CommandUtils.printDetail("Data de Atualização", FormatUtils.formatDateTime(usuarioResponse.updatedAt()));
    }

    private void atualizar(String[] subArgs) {

        if (subArgs.length != 1) {
            CommandUtils.displayCommandHelp(this, true);
            return;
        }

        String login = subArgs[0];

        if (StringUtils.isBlank(login)) {
            ConsoleUtils.printStyledError("Login não pode ser vazio.");
            return;
        }

        UsuarioService usuarioService = UsuarioService.getInstance();
        LineReader reader = Main.getReader();

        String nome = reader.readLine("Nome: ");
        String email = reader.readLine("Email: ");
        String senha = reader.readLine("Senha: ");

        UpdateUserDto updateUsuarioDto = new UpdateUserDto(login, nome, email, senha);

        Set<ConstraintViolation<UpdateUserDto>> violations = ValidationUtils.validate(updateUsuarioDto);

        if (!Objects.requireNonNullElse(violations, new HashSet<>()).isEmpty()) {
            CommandUtils.printViolations(violations, "Usuário não atualizado.");
            return;
        }

        if (!CommandUtils.confirmOperation("Deseja atualizar este usuario?")) {
            return;
        }

        try {
            usuarioService.updateUser(updateUsuarioDto);
            ConsoleUtils.printStyledSuccess("Usuario atualizado com sucesso.");
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao atualizar usuario.");
        }
    }

    private void remover(String[] subArgs) {

        if (subArgs.length != 1) {
            CommandUtils.displayCommandHelp(this, true);
            return;
        }

        String login = subArgs[0];

        if (StringUtils.isBlank(login)) {
            ConsoleUtils.printStyledError("Login não pode ser vazio.");
            return;
        }

        if (!CommandUtils.confirmOperation("Deseja remover este usuario? (Todos os dados associados serão removidos)")) {
            return;
        }

        try {

            if (UsuarioService.getInstance().deleteUser(login)) {
                ConsoleUtils.printStyledSuccess("Usuario removido com sucesso.");
            } else {
                ConsoleUtils.printStyledError("Usuario não encontrado.");
            }
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao remover usuario.");
        }
    }

    private void adicionar(String[] subArgs) {

        UsuarioService usuarioService = UsuarioService.getInstance();
        LineReader reader = Main.getReader();

        String login = reader.readLine("Login: ");
        String nome = reader.readLine("Nome: ");
        String email = reader.readLine("Email: ");
        String senha = reader.readLine("Senha: ");

        CreateUserDto createUsuarioDto = new CreateUserDto(login, nome, email, senha);

        Set<ConstraintViolation<CreateUserDto>> violations = ValidationUtils.validate(createUsuarioDto);

        if (!Objects.requireNonNullElse(violations, new HashSet<>()).isEmpty()) {
            CommandUtils.printViolations(violations, "Usuário não adicionado.");
            return;
        }

        try {
            usuarioService.createUser(createUsuarioDto);
            ConsoleUtils.printStyledSuccess("Usuario adicionado com sucesso.");
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao adicionar usuario.");
        }
    }

    private void listar(String[] subArgs) {

        UsuarioService usuarioService = UsuarioService.getInstance();
        List<UsuarioResponse> usuarios;

        try {
            usuarios = usuarioService.viewAllUsuarios();
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao buscar usuarios.");
            return;
        }

        if (usuarios.isEmpty()) {
            ConsoleUtils.printStyledWarning("Nenhum usuario cadastrado.");
            return;
        }

        ConsoleUtils.printWithTypingEffect("Lista de usuarios:");

        for (UsuarioResponse usuario : usuarios) {
            CommandUtils.printDetail("Login", usuario.login());
            CommandUtils.printDetail("Nome", usuario.nome());
            CommandUtils.printDetail("Email", usuario.email());
            CommandUtils.printDetail("Data de Criação", FormatUtils.formatDateTime(usuario.createdAt()));
            CommandUtils.printDetail("Data de Atualização", FormatUtils.formatDateTime(usuario.updatedAt()) + "\n");
        }
    }

    // Help

    @Override
    public String getDescription() {
        return "Realiza operações relacionadas a usuários.";
    }

    @Override
    public String getSyntax() {
        return CommandUtils.createSyntax(this.getCommand(), List.of("operacao"), List.of("id"));
    }

    @Override
    public List<String> getExamples() {

        String command = this.getCommand();

        return List.of(
                command + " listar",
                command + " adicionar",
                command + " remover [login]",
                command + " atualizar [login]",
                command + " detalhar [login]"
        );
    }

    @Override
    public Completers.TreeCompleter.Node getCompleter() {
        return new Completers.TreeCompleter.Node(new StringsCompleter(getCommand()), List.of(
                Completers.TreeCompleter.node("listar", ""),
                Completers.TreeCompleter.node("adicionar", ""),
                Completers.TreeCompleter.node("remover", Completers.TreeCompleter.node("[login]")),
                Completers.TreeCompleter.node("atualizar", Completers.TreeCompleter.node("[login]")),
                Completers.TreeCompleter.node("detalhar", Completers.TreeCompleter.node("[login]"))
        ));
    }
}
