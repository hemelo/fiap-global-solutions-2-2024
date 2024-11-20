package org.global.console.cli.commands;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.global.console.dto.response.ComunidadeResponse;
import org.global.console.dto.response.FornecedorResponse;
import org.global.console.dto.response.MatchEnergeticoResponse;
import org.global.console.dto.response.PoloFornecedorResponse;
import org.global.console.exceptions.NegocioException;
import org.global.console.services.MatchEnergeticoService;
import org.global.console.utils.CommandUtils;
import org.global.console.utils.ConsoleUtils;
import org.jline.builtins.Completers;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import java.util.List;
import java.util.Optional;

public class MatchCommand implements Command {
    @Override
    public void execute(String[] args) {

        if (args.length == 0) {
            CommandUtils.displayCommandHelp(this, true);
            return;
        }

        String operacao = args[0];
        String[] subArgs = ArrayUtils.remove(args, 0);

        switch (operacao) {
            case "listar":
                listar(subArgs);
                break;
            default:
                CommandUtils.displayCommandHelp(this, true);
        }
    }

    private void listar(String[] args) {

        MatchEnergeticoService matchEnergeticoService = MatchEnergeticoService.getInstance();

        if (args.length == 1) {

            long id;

            try {
                id = Long.parseLong(args[0]);
            } catch (NumberFormatException e) {
                ConsoleUtils.printStyledError("O ID da comunidade deve ser um número inteiro.");
                return;
            }

            try {

                ConsoleUtils.printWithTypingEffect("Realizando match energético para a comunidade...");

                List<MatchEnergeticoResponse> matchEnergetico = matchEnergeticoService.realizarMatchEnergetico(id);

                if (CollectionUtils.isEmpty(matchEnergetico)) {
                    ConsoleUtils.printStyledWarning("Não foram encontrados matches energéticos para a comunidade.");
                    return;
                }

                String comunidade = Optional.ofNullable(matchEnergetico.get(0).getComunidade()).map(ComunidadeResponse::nome).orElse(String.valueOf(id));

                ConsoleUtils.printWithTypingEffect("Match energético realizado com sucesso!");
                ConsoleUtils.printWithTypingEffect("Listando matches energéticos para a comunidade " + comunidade + "...\n");

                for (MatchEnergeticoResponse match : matchEnergetico) {
                    CommandUtils.printDetail("Fornecedor", Optional.ofNullable(match.getFornecedor()).map(FornecedorResponse::nome).orElse(String.valueOf(match.getFornecedorId())));
                    CommandUtils.printDetail("Polo de fornecimento", Optional.ofNullable(match.getPolo()).map(PoloFornecedorResponse::nome).orElse(String.valueOf(match.getPoloId())));
                    CommandUtils.printDetail("Energia", Optional.ofNullable(match.getEnergia()).map(energiaResponse -> energiaResponse.nome() + " - " + energiaResponse.tipo() ).orElse(String.valueOf(match.getEnergiaId())));

                    if (match.getDistancia() != null) {
                        CommandUtils.printDetail("Distância do polo", match.getDistancia() + " km");
                    }

                    CommandUtils.printDetail("População a ser atendida", match.getPopulacaoASerAtendida() + " pessoas (" + match.getPercentualPopulacaoAtendidaEmRelacaoAoDeficit() + "% de " + match.getPopulacaoDeficitInicial() + " e " + match.getPercentualPopulacaoAtendidaEmRelacaoPopulacaoTotal() + "% da população total " + Optional.ofNullable(match.getComunidade()).map(ComunidadeResponse::populacao).orElse(null) + ")");
                    CommandUtils.printDetail("Capacidade restante do polo", match.getCapacidadeMaximaPoloRestante() + " pessoas (" + match.getPercentualCapacidadePoloRestante() + "%)");

                    if (matchEnergetico.size() > 1) {
                        CommandUtils.printDetail("Rank de distância", String.valueOf(match.getRankDistancia()) + " de " + matchEnergetico.size() + " matches (menor é melhor)");
                        CommandUtils.printDetail("Rank de população a ser atendida", String.valueOf(match.getRankSuprimentoDeficit()) + " de " + matchEnergetico.size() + " matches (menor é melhor)");
                    }

                    ConsoleUtils.printWithTypingEffect("");
                }

                ConsoleUtils.printStyledSuccess("\nGostou de algum match? Basta registrar o fornecimento energético para a comunidade com o comando '" + new AttributedString("fornecedor disponibilizacao adicionar", AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)).toAnsi() + "'.");

            } catch (NegocioException e) {
                ConsoleUtils.printStyledWarning(e.getMessage());
            } catch (Exception e) {
                ConsoleUtils.printStyledError("Erro ao listar matches energéticos para a comunidade.");
            }

            return;
        }

        throw new NotImplementedException("Operação não implementada.");
    }

    @Override
    public String getDescription() {
        return "Responsável por realizar match entre comunidades e polos de fornecedor. É considerado a necessidade atual de atendimento à comunidade e a capacidade de atendimento do polo.";
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
                command + " listar [comunidade_id]");
    }

    @Override
    public Completers.TreeCompleter.Node getCompleter() {
        return new Completers.TreeCompleter.Node(new StringsCompleter(getCommand()), List.of(
                Completers.TreeCompleter.node("listar", ""),
                Completers.TreeCompleter.node("listar", Completers.TreeCompleter.node("[íd]"))
        ));
    }
}
