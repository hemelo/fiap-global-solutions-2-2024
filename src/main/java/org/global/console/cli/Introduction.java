package org.global.console.cli;

import com.github.lalyos.jfiglet.FigletFont;
import org.global.console.utils.CommandUtils;
import org.global.console.utils.ConsoleUtils;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

public class Introduction {

    public static void display() {
        try {

            String asciiArt = FigletFont.convertOneLine("classpath:/fonts/Slant.flf", "E C O S Y N C");
            System.out.println(new AttributedString(
                    asciiArt,
                    AttributedStyle.BOLD.foreground(AttributedStyle.GREEN)).toAnsi());


        } catch (Exception e) {
            System.out.println("EcoSync");
        }

        // Apresentação do programa
        String introduction = "Bem-vindo ao EcoSync - Uma plataforma para acesso à energia sustentável em comunidades isoladas!\n" +
                "Este projeto foi desenvolvido para promover inclusão social, sustentabilidade e inovação tecnológica, conectando comunidades sem acesso adequado à energia com doadores e empresas comprometidos em apoiar essa causa.\n\n" +
                "Com uma abordagem colaborativa e focada no impacto real, o EcoSync facilita a criação de soluções energéticas sustentáveis e transforma vidas por meio do fornecimento de infraestrutura essencial.\n\n" +
                "Características principais:\n" +
                "  - Cadastro de Comunidades e Doadores: Permita que comunidades registrem suas necessidades enquanto doadores e empresas oferecem apoio financeiro ou logístico.\n" +
                "  - Banco de Dados com Informações em Tempo Real: Monitore dados energéticos, previsão de consumo e relatórios de impacto para identificar soluções eficientes.\n" +
                "  - Mecanismo de Matching (Conexão): Conecte doadores e comunidades com base em critérios como localização e tipo de energia necessária.\n" +
                "  - Relatórios de Impacto: Acompanhe o impacto ambiental e econômico em tempo real, incentivando maior engajamento.\n" +
                "  - Sistema de Planejamento Energético: Otimize recursos e escolha fontes de energia renovável mais adequadas, como solar ou eólica.\n\n" +
                "Estamos felizes em tê-lo conosco nessa jornada de transformação social e sustentabilidade. Esperamos que o EcoSync inspire colaboração e crie um futuro mais justo e sustentável para todos.\n\n" +
                "Vamos começar?\n";

        // Exibir a apresentação com estilização
        ConsoleUtils.printWithTypingEffect(new AttributedString(
                introduction,
                AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)).toAnsi());

        String ajuda1 = "Comece digitando ";
        String ajuda2 = "help";
        String ajuda3 = " para ver a lista de comandos disponíveis. Ou pressione ";
        String ajuda4 = "TAB";
        String ajuda5 = " para completar comandos.";

        System.out.print(new AttributedString(
                ajuda1,
                AttributedStyle.DEFAULT.foreground(CommandUtils.ColorExtensions.GRAY)).toAnsi());

        System.out.print(new AttributedString(
                ajuda2,
                AttributedStyle.BOLD.foreground(AttributedStyle.BLACK).background(AttributedStyle.GREEN)).toAnsi());

        ConsoleUtils.printWithTypingEffect(new AttributedString(
                ajuda3,
                AttributedStyle.DEFAULT.foreground(CommandUtils.ColorExtensions.GRAY)).toAnsi());

        System.out.print(new AttributedString(
                ajuda4,
                AttributedStyle.BOLD.foreground(AttributedStyle.BLACK).background(AttributedStyle.GREEN)).toAnsi());

        ConsoleUtils.printWithTypingEffect(new AttributedString(
                ajuda5,
                AttributedStyle.DEFAULT.foreground(CommandUtils.ColorExtensions.GRAY)).toAnsi());

    }
}
