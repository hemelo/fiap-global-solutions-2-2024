package org.global.console.cli.commands;

import jakarta.validation.ConstraintViolation;
import org.apache.commons.lang3.ArrayUtils;
import org.global.console.Main;
import org.global.console.dto.request.create.CreateComunidadeDto;
import org.global.console.dto.request.update.UpdateComunidadeDto;
import org.global.console.dto.response.ComunidadeResponse;
import org.global.console.services.ComunidadeService;
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

public class ComunidadeCommand implements Command {
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

        long id;

        try {
            id = Long.parseLong(subArgs[0]);
        } catch (NumberFormatException e) {
            ConsoleUtils.printStyledError("O ID do comunidade deve ser um número inteiro.");
            return;
        }

        ComunidadeResponse comunidadeResponse;

        try {
            comunidadeResponse = ComunidadeService.getInstance().viewComunidade(id);
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao buscar comunidade");
            return;
        }

        if (comunidadeResponse == null) {
            ConsoleUtils.printStyledError("Comunidade não encontrado.");
            return;
        }

        ConsoleUtils.printWithTypingEffect("Detalhes do comunidade:");

        CommandUtils.printDetail("ID", String.valueOf(comunidadeResponse.id()));
        CommandUtils.printDetail("Nome", comunidadeResponse.nome());
        CommandUtils.printDetail("Localizacao", comunidadeResponse.localizacao());
        CommandUtils.printDetail("Descrição", comunidadeResponse.descricao());
        CommandUtils.printDetail("População", String.valueOf(comunidadeResponse.populacao()));
        CommandUtils.printDetail("Latitude", String.valueOf(comunidadeResponse.latitude()));
        CommandUtils.printDetail("Longitude", String.valueOf(comunidadeResponse.longitude()));
        CommandUtils.printDetail("Data de Criação", FormatUtils.formatDateTime(comunidadeResponse.createdAt()));
        CommandUtils.printDetail("Data de Atualização\n", FormatUtils.formatDateTime(comunidadeResponse.updatedAt()));
    }

    private void atualizar(String[] subArgs) {

        if (subArgs.length != 1) {
            CommandUtils.displayCommandHelp(this, true);
            return;
        }

        long id;

        try {
            id = Long.parseLong(subArgs[0]);
        } catch (NumberFormatException e) {
            ConsoleUtils.printStyledError("O ID do comunidade deve ser um número inteiro.");
            return;
        }

        ComunidadeService comunidadeService = ComunidadeService.getInstance();
        LineReader reader = Main.getReader();

        Long populacao = null;
        Double latitude = null, longitude = null;
        String nome = reader.readLine("Nome: ");
        String localizacao = reader.readLine("Localização: ");
        String descricao = reader.readLine("Descrição: ");

        while (populacao == null) {
            try {
                populacao = Long.parseLong(reader.readLine("População: "));
            } catch (NumberFormatException e) {
                ConsoleUtils.printStyledError("A população deve ser um número.");
            }
        }

        while (latitude == null) {
            try {
                latitude = Double.parseDouble(reader.readLine("Latitude: "));
            } catch (NumberFormatException e) {
                ConsoleUtils.printStyledError("A latitude deve ser um número.");
            }
        }

        while (longitude == null) {
            try {
                longitude = Double.parseDouble(reader.readLine("Longitude: "));
            } catch (NumberFormatException e) {
                ConsoleUtils.printStyledError("A longitude deve ser um número.");
            }
        }

        UpdateComunidadeDto updateComunidadeDto = new UpdateComunidadeDto(id, nome, localizacao, descricao, latitude, longitude, populacao);

        Set<ConstraintViolation<UpdateComunidadeDto>> violations = ValidationUtils.validate(updateComunidadeDto);

        if (!Objects.requireNonNullElse(violations, new HashSet<>()).isEmpty()) {
            CommandUtils.printViolations(violations, "Comunidade não atualizada.");
            return;
        }

        if (!CommandUtils.confirmOperation("Deseja atualizar este comunidade?")) {
            return;
        }

        try {
            comunidadeService.updateComunidade(updateComunidadeDto);
            ConsoleUtils.printStyledSuccess("Comunidade atualizado com sucesso.");
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao atualizar comunidade.");
        }
    }

    private void remover(String[] subArgs) {

        if (subArgs.length != 1) {
            CommandUtils.displayCommandHelp(this, true);
            return;
        }

        long id;

        try {
            id = Long.parseLong(subArgs[0]);
        } catch (NumberFormatException e) {
            ConsoleUtils.printStyledError("O ID do comunidade deve ser um número inteiro.");
            return;
        }

        if (!CommandUtils.confirmOperation("Deseja remover este comunidade? (Todos os dados associados serão removidos)")) {
            return;
        }

        try {
            ComunidadeService.getInstance().deleteComunidade(id);
            ConsoleUtils.printStyledSuccess("Comunidade removido com sucesso.");
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao remover comunidade.");
        }
    }

    private void adicionar(String[] subArgs) {

        ComunidadeService comunidadeService = ComunidadeService.getInstance();
        LineReader reader = Main.getReader();

        Long populacao = null;
        Double latitude = null, longitude = null;
        String nome = reader.readLine("Nome: ");
        String localizacao = reader.readLine("Localização: ");
        String descricao = reader.readLine("Descrição: ");

        while (populacao == null) {
            try {
                populacao = Long.parseLong(reader.readLine("População: "));
            } catch (NumberFormatException e) {
                ConsoleUtils.printStyledError("A população deve ser um número.");
            }
        }

        while (latitude == null) {
            try {
                latitude = Double.parseDouble(reader.readLine("Latitude: "));
            } catch (NumberFormatException e) {
                ConsoleUtils.printStyledError("A latitude deve ser um número.");
            }
        }

        while (longitude == null) {
            try {
                longitude = Double.parseDouble(reader.readLine("Longitude: "));
            } catch (NumberFormatException e) {
                ConsoleUtils.printStyledError("A longitude deve ser um número.");
            }
        }

        CreateComunidadeDto createComunidadeDto = new CreateComunidadeDto(nome, localizacao, descricao, latitude, longitude , populacao);

        Set<ConstraintViolation<CreateComunidadeDto>> violations = ValidationUtils.validate(createComunidadeDto);

        if (!Objects.requireNonNullElse(violations, new HashSet<>()).isEmpty()) {
            CommandUtils.printViolations(violations, "Comunidade não adicionada.");
            return;
        }

        try {
            comunidadeService.createComunidade(createComunidadeDto);
            ConsoleUtils.printStyledSuccess("Comunidade adicionado com sucesso.");
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao adicionar comunidade.");
        }
    }

    private void listar(String[] subArgs) {

        ComunidadeService comunidadeService = ComunidadeService.getInstance();
        List<ComunidadeResponse> comunidadees;

        try {
            comunidadees = comunidadeService.viewAllComunidades();
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao buscar comunidadees.");
            return;
        }

        if (comunidadees.isEmpty()) {
            ConsoleUtils.printStyledWarning("Nenhum comunidade cadastrado.");
            return;
        }

        ConsoleUtils.printWithTypingEffect("Lista de comunidadees:");

        for (ComunidadeResponse comunidade : comunidadees) {
            CommandUtils.printDetail("ID", String.valueOf(comunidade.id()));
            CommandUtils.printDetail("Nome", comunidade.nome());
            CommandUtils.printDetail("Localizacao", comunidade.localizacao());
            CommandUtils.printDetail("Descrição", comunidade.descricao());
            CommandUtils.printDetail("População", String.valueOf(comunidade.populacao()));
            CommandUtils.printDetail("Latitude", String.valueOf(comunidade.latitude()));
            CommandUtils.printDetail("Longitude", String.valueOf(comunidade.longitude()));
            CommandUtils.printDetail("Data de Criação", FormatUtils.formatDateTime(comunidade.createdAt()));
            CommandUtils.printDetail("Data de Atualização", FormatUtils.formatDateTime(comunidade.updatedAt()) + "\n");
        }
    }


    // Help

    @Override
    public String getDescription() {
        return "Realiza operações relacionadas a comunidades.";
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
                command + " remover [id]",
                command + " atualizar [id]",
                command + " detalhar [id]"
        );
    }

    @Override
    public Completers.TreeCompleter.Node getCompleter() {
        return new Completers.TreeCompleter.Node(new StringsCompleter(getCommand()), List.of(
                Completers.TreeCompleter.node("listar", ""),
                Completers.TreeCompleter.node("adicionar", ""),
                Completers.TreeCompleter.node("remover", Completers.TreeCompleter.node("[íd]")),
                Completers.TreeCompleter.node("atualizar", Completers.TreeCompleter.node("[íd]")),
                Completers.TreeCompleter.node("detalhar", Completers.TreeCompleter.node("[íd]"))
        ));
    }
}