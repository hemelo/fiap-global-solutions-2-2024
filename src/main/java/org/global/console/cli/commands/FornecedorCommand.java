package org.global.console.cli.commands;

import jakarta.validation.ConstraintViolation;
import org.apache.commons.lang3.ArrayUtils;
import org.global.console.Main;
import org.global.console.dto.request.create.CreateEnergiaDto;
import org.global.console.dto.request.create.CreateFornecedorDto;
import org.global.console.dto.request.create.CreatePoloFornecedorDto;
import org.global.console.dto.request.update.UpdateFornecedorDto;
import org.global.console.dto.request.update.UpdatePoloFornecedorDto;
import org.global.console.dto.response.FornecedorResponse;
import org.global.console.dto.response.PoloFornecedorResponse;
import org.global.console.services.FornecedorService;
import org.global.console.utils.CommandUtils;
import org.global.console.utils.ConsoleUtils;
import org.global.console.utils.FormatUtils;
import org.global.console.utils.ValidationUtils;
import org.jline.builtins.Completers;
import org.jline.reader.LineReader;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import java.util.*;

/**
 * Classe que implementa o comando relacionado a fornecedores.
 */
public class FornecedorCommand implements Command {
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
                adicionar(subArgs);
                break;
            case "remover":
                remover(subArgs);
                break;
            case "atualizar":
                atualizar(subArgs);
                break;
            case "detalhar":
                detalhar(subArgs);
                break;
            case "polo":
                if (subArgs.length == 0) {
                    CommandUtils.displayCommandHelp(this, true);
                    return;
                }

                String[] subSubArgs = new String[subArgs.length - 1];
                subSubArgs = ArrayUtils.remove(subArgs, 0);

                switch (subArgs[0]) {
                    case "adicionar":
                        adicionarPolo(subSubArgs);
                        break;
                    case "remover":
                        removerPolo(subSubArgs);
                        break;
                    case "atualizar":
                        atualizarPolo(subSubArgs);
                        break;
                    case "detalhar":
                        detalharPolo(subSubArgs);
                        break;
                    case "listar":
                        listarPolos(subSubArgs);
                        break;
                    default:
                        CommandUtils.displayCommandHelp(this, true);
                }
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
            ConsoleUtils.printStyledError("O ID do fornecedor deve ser um número inteiro.");
            return;
        }

        FornecedorResponse fornecedorResponse;

        try {
            fornecedorResponse = FornecedorService.getInstance().viewFornecedor(id);
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao buscar fornecedor");
            return;
        }

        if (fornecedorResponse == null) {
            ConsoleUtils.printStyledError("Fornecedor não encontrado.");
            return;
        }

        ConsoleUtils.printWithTypingEffect("Detalhes do fornecedor:");

        CommandUtils.printDetail("ID", String.valueOf(fornecedorResponse.id()));
        CommandUtils.printDetail("Nome", fornecedorResponse.nome());
        CommandUtils.printDetail("Endereço", fornecedorResponse.endereco());
        CommandUtils.printDetail("CNPJ", fornecedorResponse.cnpj());
        CommandUtils.printDetail("Data de Criação", FormatUtils.formatDateTime(fornecedorResponse.createdAt()));
        CommandUtils.printDetail("Data de Atualização", FormatUtils.formatDateTime(fornecedorResponse.updatedAt()) + "\n");

        if (fornecedorResponse.polos().isEmpty()) {
            ConsoleUtils.printStyledWarning("Nenhum polo cadastrado para este fornecedor.");
            return;
        }

        for (PoloFornecedorResponse polo : fornecedorResponse.polos()) {
            CommandUtils.printDetail("Polo " + polo.id(), polo.nome());
            CommandUtils.printDetail("Endereço", polo.endereco());
            CommandUtils.printDetail("Latitude", String.valueOf(polo.latitude()));
            CommandUtils.printDetail("Longitude", String.valueOf(polo.longitude()));
        }
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
            ConsoleUtils.printStyledError("O ID do fornecedor deve ser um número inteiro.");
            return;
        }

        FornecedorService fornecedorService = FornecedorService.getInstance();
        LineReader reader = Main.getReader();

        String nome = reader.readLine("Nome: ");
        String endereco = reader.readLine("Endereço: ");
        String cnpj = reader.readLine("CNPJ: ");

        UpdateFornecedorDto updateFornecedorDto = new UpdateFornecedorDto(id, nome, cnpj, endereco);

        Set<ConstraintViolation<UpdateFornecedorDto>> violations = ValidationUtils.validate(updateFornecedorDto);

        if (!Objects.requireNonNullElse(violations, new HashSet<>()).isEmpty()) {
            CommandUtils.printViolations(violations, "Fornecedor não adicionado.");
            return;
        }

        if (!CommandUtils.confirmOperation("Deseja atualizar este fornecedor?")) {
            return;
        }

        try {
            fornecedorService.updateFornecedor(updateFornecedorDto);
            ConsoleUtils.printStyledSuccess("Fornecedor atualizado com sucesso.");
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao atualizar fornecedor.");
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
            ConsoleUtils.printStyledError("O ID do fornecedor deve ser um número inteiro.");
            return;
        }

        if (!CommandUtils.confirmOperation("Deseja remover este fornecedor? (Todos os dados associados serão removidos)")) {
            return;
        }

        try {
            FornecedorService.getInstance().deleteFornecedor(id);
            ConsoleUtils.printStyledSuccess("Fornecedor removido com sucesso.");
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao remover fornecedor.");
        }
    }

    private void adicionar(String[] subArgs) {

        FornecedorService fornecedorService = FornecedorService.getInstance();
        LineReader reader = Main.getReader();

        String nome = reader.readLine("Nome: ");
        String endereco = reader.readLine("Endereço: ");
        String cnpj = reader.readLine("CNPJ: ");

        CreateFornecedorDto createFornecedorDto = new CreateFornecedorDto(nome, cnpj, endereco);

        Set<ConstraintViolation<CreateFornecedorDto>> violations = ValidationUtils.validate(createFornecedorDto);

        if (!Objects.requireNonNullElse(violations, new HashSet<>()).isEmpty()) {
            CommandUtils.printViolations(violations, "Fornecedor não adicionado.");
            return;
        }

        try {
            fornecedorService.createFornecedor(createFornecedorDto);
            ConsoleUtils.printStyledSuccess("Fornecedor adicionado com sucesso.");
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao adicionar fornecedor.");
        }
    }

    private void listar(String[] subArgs) {

        List<FornecedorResponse> fornecedores;

        try {
            fornecedores = FornecedorService.getInstance().viewAllFornecedores();
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao buscar fornecedores.");
            return;
        }

        if (fornecedores.isEmpty()) {
            ConsoleUtils.printStyledWarning("Nenhum fornecedor cadastrado.");
            return;
        }

        ConsoleUtils.printWithTypingEffect("Lista de fornecedores:");

        for (FornecedorResponse fornecedor : fornecedores) {
            CommandUtils.printDetail("ID", String.valueOf(fornecedor.id()));
            CommandUtils.printDetail("Nome", fornecedor.nome());
            CommandUtils.printDetail("Endereço", fornecedor.endereco());
            CommandUtils.printDetail("CNPJ", fornecedor.cnpj());
            CommandUtils.printDetail("Quantidade de Polos", String.valueOf(Objects.requireNonNullElse(fornecedor.polos(), new ArrayList<>()).size()));
            CommandUtils.printDetail("Data de Criação", FormatUtils.formatDateTime(fornecedor.createdAt()));
            CommandUtils.printDetail("Data de Atualização", FormatUtils.formatDateTime(fornecedor.updatedAt()) + "\n");
        }
    }

    private void adicionarPolo(String[] subArgs) {

        FornecedorService fornecedorService = FornecedorService.getInstance();
        LineReader reader = Main.getReader();

        Double latitude = null, longitude = null;
        Long idFornecedor = null;

        while (idFornecedor == null) {
            try {
                idFornecedor = Long.parseLong(reader.readLine("ID do fornecedor: "));
            } catch (NumberFormatException e) {
                ConsoleUtils.printStyledError("O ID do fornecedor deve ser um número inteiro.");
            }
        }

        String nome = reader.readLine("Nome: ");
        String endereco = reader.readLine("Endereço: ");

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

        CreatePoloFornecedorDto createPoloFornecedorDto = new CreatePoloFornecedorDto(
                nome, endereco, latitude, longitude, idFornecedor
        );

        if (!CommandUtils.confirmOperation("Deseja adicionar este polo fornecedor?")) {
            return;
        }

        try {
            fornecedorService.createPoloFornecedor(createPoloFornecedorDto);
            ConsoleUtils.printStyledSuccess("Polo fornecedor adicionado com sucesso.");
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao adicionar polo fornecedor.");
        }
    }

    private void atualizarPolo(String[] subArgs) {

        if (subArgs.length != 1) {
            CommandUtils.displayCommandHelp(this, true);
            return;
        }

        long id;

        try {
            id = Long.parseLong(subArgs[0]);
        } catch (NumberFormatException e) {
            ConsoleUtils.printStyledError("O ID do polo fornecedor deve ser um número inteiro.");
            return;
        }

        FornecedorService fornecedorService = FornecedorService.getInstance();
        LineReader reader = Main.getReader();

        Double latitude = null, longitude = null;
        Long idFornecedor = null;

        while (idFornecedor == null) {
            try {
                idFornecedor = Long.parseLong(reader.readLine("ID do fornecedor: "));
            } catch (NumberFormatException e) {
                ConsoleUtils.printStyledError("O ID do fornecedor deve ser um número inteiro.");
            }
        }

        String nome = reader.readLine("Nome: ");
        String endereco = reader.readLine("Endereço: ");

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

        UpdatePoloFornecedorDto updatePoloFornecedorDto = new UpdatePoloFornecedorDto(id, nome, endereco, latitude, longitude, idFornecedor);

        if (!CommandUtils.confirmOperation("Deseja atualizar este polo fornecedor?")) {
            return;
        }

        try {
            fornecedorService.updatePoloFornecedor(updatePoloFornecedorDto);
            ConsoleUtils.printStyledSuccess("Polo fornecedor atualizado com sucesso.");
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao atualizar polo fornecedor.");
        }
    }

    public void removerPolo(String[] subArgs) {

        if (subArgs.length != 1) {
            CommandUtils.displayCommandHelp(this, true);
            return;
        }

        long id;

        try {
            id = Long.parseLong(subArgs[0]);
        } catch (NumberFormatException e) {
            ConsoleUtils.printStyledError("O ID do polo fornecedor deve ser um número inteiro.");
            return;
        }

        if (!CommandUtils.confirmOperation("Deseja remover este polo fornecedor?")) {
            return;
        }

        try {
            FornecedorService.getInstance().deletePoloFornecedor(id);
            ConsoleUtils.printStyledSuccess("Polo fornecedor removido com sucesso.");
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao remover polo fornecedor.");
        }
    }

    public void detalharPolo(String[] subArgs) {
        if (subArgs.length != 1) {
            CommandUtils.displayCommandHelp(this, true);
            return;
        }

        long id;

        try {
            id = Long.parseLong(subArgs[0]);
        } catch (NumberFormatException e) {
            ConsoleUtils.printStyledError("O ID do polo fornecedor deve ser um número inteiro.");
            return;
        }

        PoloFornecedorResponse poloFornecedorResponse;

        try {
            poloFornecedorResponse = FornecedorService.getInstance().viewPoloFornecedor(id);
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao buscar polo fornecedor");
            return;
        }

        if (poloFornecedorResponse == null) {
            ConsoleUtils.printStyledError("Polo Fornecedor não encontrado.");
            return;
        }

        ConsoleUtils.printWithTypingEffect("Detalhes do polo fornecedor:");

        CommandUtils.printDetail("ID", String.valueOf(poloFornecedorResponse.id()));
        CommandUtils.printDetail("Nome", poloFornecedorResponse.nome());
        CommandUtils.printDetail("Endereço", poloFornecedorResponse.endereco());
        CommandUtils.printDetail("Fornecedor ID", String.valueOf(poloFornecedorResponse.idFornecedor()));
        CommandUtils.printDetail("Latitude", String.valueOf(poloFornecedorResponse.latitude()));
        CommandUtils.printDetail("Longitude", String.valueOf(poloFornecedorResponse.longitude()));
    }

    public void listarPolos(String[] subArgs) {
        FornecedorService fornecedorService = FornecedorService.getInstance();
        List<PoloFornecedorResponse> polos;

        try {
            polos = fornecedorService.viewAllPolosFornecedor();
        } catch (Exception e) {
            ConsoleUtils.printStyledError("Erro ao buscar polos fornecedor.");
            return;
        }

        if (polos.isEmpty()) {
            ConsoleUtils.printStyledWarning("Nenhum polo fornecedor cadastrado.");
            return;
        }

        ConsoleUtils.printWithTypingEffect("Lista de polos fornecedor:");

        for (PoloFornecedorResponse polo : polos) {
            CommandUtils.printDetail("ID", String.valueOf(polo.id()));
            CommandUtils.printDetail("Nome", polo.nome());
            CommandUtils.printDetail("Endereço", polo.endereco());
            CommandUtils.printDetail("Fornecedor ID", String.valueOf(polo.idFornecedor()));
            CommandUtils.printDetail("Latitude", String.valueOf(polo.latitude()));
            CommandUtils.printDetail("Longitude", String.valueOf(polo.longitude()) + "\n");
        }
    }

    // Help

    @Override
    public String getDescription() {
        return "Realiza operações relacionadas a fornecedores.";
    }

    @Override
    public String getSyntax() {
        return CommandUtils.createSyntax(this.getCommand(), List.of("operacao"), List.of("id"));
    }

    @Override
    public String getOptions() {
        return "";
    }

    @Override
    public List<String> getExamples() {

        String command = this.getCommand();

        return List.of(
                command + " listar",
                command + " adicionar",
                command + " remover [id]",
                command + " atualizar [id]",
                command + " detalhar [id]",
                command + " polo listar",
                command + " polo adicionar",
                command + " polo remover [id]",
                command + " polo atualizar [id]",
                command + " polo detalhar [id]"
        );
    }

    @Override
    public Completers.TreeCompleter.Node getCompleter() {
        return new Completers.TreeCompleter.Node(new StringsCompleter(getCommand()), List.of(
                new Completers.TreeCompleter.Node(new StringsCompleter(new AttributedString(
                        "polo", AttributedStyle.BOLD.foreground(AttributedStyle.BLUE)).toAnsi()
                ), List.of(
                        Completers.TreeCompleter.node("listar", ""),
                        Completers.TreeCompleter.node("adicionar", ""),
                        Completers.TreeCompleter.node("remover", Completers.TreeCompleter.node("[íd]")),
                        Completers.TreeCompleter.node("atualizar", Completers.TreeCompleter.node("[íd]")),
                        Completers.TreeCompleter.node("detalhar", Completers.TreeCompleter.node("[íd]"))
                )),
                Completers.TreeCompleter.node("listar", ""),
                Completers.TreeCompleter.node("adicionar", ""),
                Completers.TreeCompleter.node("remover", Completers.TreeCompleter.node("[íd]")),
                Completers.TreeCompleter.node("atualizar", Completers.TreeCompleter.node("[íd]")),
                Completers.TreeCompleter.node("detalhar", Completers.TreeCompleter.node("[íd]"))
        ));
    }
}
