package org.global.console.cli.commands;

import jakarta.validation.ConstraintViolation;
import org.apache.commons.lang3.ArrayUtils;
import org.global.console.Main;
import org.global.console.dto.request.create.CreateEnergiaDto;
import org.global.console.dto.request.update.UpdateEnergiaDto;
import org.global.console.dto.response.EnergiaResponse;
import org.global.console.enums.UnidadeMedida;
import org.global.console.services.EnergiaService;
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

public class EnergiaCommand implements Command {

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
            ConsoleUtils.printStyledError("O ID da fonte de energia deve ser um número inteiro.");
            return;
        }

        EnergiaService energiaService = EnergiaService.getInstance();
        EnergiaResponse energiaResponse;

        try {
            energiaResponse = energiaService.viewEnergia(id);
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao buscar fonte de energia");
            return;
        }

        if (energiaResponse == null) {
            ConsoleUtils.printStyledError("Fotne de energia não encontrada.");
            return;
        }

        ConsoleUtils.printWithTypingEffect("Detalhes da fonte de energia:");

        CommandUtils.printDetail("ID", String.valueOf(energiaResponse.id()));
        CommandUtils.printDetail("Nome", energiaResponse.nome());
        CommandUtils.printDetail("Tipo", energiaResponse.tipo());
        CommandUtils.printDetail("Descrição", energiaResponse.descricao());
        //CommandUtils.printDetail("Unidade", energiaResponse.unidade());
        CommandUtils.printDetail("Data de Criação", FormatUtils.formatDateTime(energiaResponse.createdAt()));
        CommandUtils.printDetail("Data de Atualização", FormatUtils.formatDateTime(energiaResponse.updatedAt()) + "\n");
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
            ConsoleUtils.printStyledError("O ID da fonte de energia deve ser um número inteiro.");
            return;
        }

        EnergiaService energiaService = EnergiaService.getInstance();
        LineReader reader = Main.getReader();

        String nome = reader.readLine("Nome: ");
        String descricao = reader.readLine("Descrição: ");

        UnidadeMedida unidade = null;

        /*while (unidade == null) {
            String unidadeStr = reader.readLine("Unidade de Medida: ");
            unidade = UnidadeMedida.fromValue(unidadeStr);

            if (unidade == null) {
                ConsoleUtils.printStyledError("Unidade de medida inválida.");
            }
        }*/

        String tipo = reader.readLine("Tipo: ");

        UpdateEnergiaDto updateEnergiaDto = new UpdateEnergiaDto(id, nome, descricao, unidade, tipo);

        Set<ConstraintViolation<UpdateEnergiaDto>> violations = ValidationUtils.validate(updateEnergiaDto);

        if (!Objects.requireNonNullElse(violations, new HashSet<>()).isEmpty()) {
            CommandUtils.printViolations(violations, "Fonte de energia não atualizada.");
            return;
        }

        if (!CommandUtils.confirmOperation("Deseja atualizar esta fonte de energia?")) {
            return;
        }

        try {
            energiaService.updateEnergia(updateEnergiaDto);
            ConsoleUtils.printStyledSuccess("Fonte de energia atualizada com sucesso.");
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao atualizar energia.");
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
            ConsoleUtils.printStyledError("O ID da fonte de energia deve ser um número inteiro.");
            return;
        }

        if (!CommandUtils.confirmOperation("Deseja remover esta fonte de energia? (Todos os dados associados serão removidos)")) {
            return;
        }

        try {
            EnergiaService.getInstance().deleteEnergia(id);
            ConsoleUtils.printStyledSuccess("Fonte de energia removida com sucesso.");
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao remover fonte de energia.");
        }
    }

    private void adicionar(String[] subArgs) {

        EnergiaService energiaService = EnergiaService.getInstance();
        LineReader reader = Main.getReader();

        String nome = reader.readLine("Nome: ");
        String descricao = reader.readLine("Descrição: ");

        UnidadeMedida unidade = null;

        /*while (unidade == null) {
            String unidadeStr = reader.readLine("Unidade de Medida: ");
            unidade = UnidadeMedida.fromValue(unidadeStr);

            if (unidade == null) {
                ConsoleUtils.printStyledError("Unidade de medida inválida.");
            }
        }*/

        String tipo = reader.readLine("Tipo: ");

        CreateEnergiaDto createEnergiaDto = new CreateEnergiaDto(nome, descricao, unidade, tipo);

        Set<ConstraintViolation<CreateEnergiaDto>> violations = ValidationUtils.validate(createEnergiaDto);

        if (!Objects.requireNonNullElse(violations, new HashSet<>()).isEmpty()) {
            CommandUtils.printViolations(violations, "Fonte de energia não adicionada.");
            return;
        }

        try {
            energiaService.createEnergia(createEnergiaDto);
            ConsoleUtils.printStyledSuccess("Fonte de energia adicionada com sucesso.");
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao adicionar fonte de energia.");
        }
    }

    private void listar(String[] subArgs) {

        EnergiaService energiaService = EnergiaService.getInstance();
        List<EnergiaResponse> energias;

        try {
            energias = energiaService.viewAllEnergias();
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao buscar fontes de energia.");
            return;
        }

        if (energias.isEmpty()) {
            ConsoleUtils.printStyledWarning("Nenhuma fonte de energia cadastrado.");
            return;
        }

        ConsoleUtils.printWithTypingEffect("Lista de fontes de energia:");

        for (EnergiaResponse energia : energias) {
            CommandUtils.printDetail("ID", String.valueOf(energia.id()));
            CommandUtils.printDetail("Nome", energia.nome());
            CommandUtils.printDetail("Tipo", energia.tipo());
            CommandUtils.printDetail("Descrição", energia.descricao());
            //CommandUtils.printDetail("Unidade", energia.unidade());
            CommandUtils.printDetail("Data de Criação", FormatUtils.formatDateTime(energia.createdAt()));
            CommandUtils.printDetail("Data de Atualização", FormatUtils.formatDateTime(energia.updatedAt()) + "\n");
        }
    }

    // Help

    @Override
    public String getDescription() {
        return "Realiza operações relacionadas a energia.";
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
