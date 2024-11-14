package org.global.console.cli;

import com.github.lalyos.jfiglet.FigletFont;
import org.global.console.utils.CommandUtils;
import org.global.console.utils.ConsoleUtils;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

public class Introduction {

    public static void display() {
        try {

            String asciiArt = FigletFont.convertOneLine("classpath:/fonts/Slant.flf", "G l o b a l   C o n s o l e");
            System.out.println(new AttributedString(
                    asciiArt,
                    AttributedStyle.BOLD.foreground(AttributedStyle.GREEN)).toAnsi());


        } catch (Exception e) {
            System.out.println("Global Console");
        }

        // Apresentação do programa
        String introduction = "Bem-vindo ao Nebula - Uma plataforma de gerenciamento de portfólios!\n" +
                "Este programa foi desenvolvido para ajudar você a gerenciar e otimizar seus investimentos.\n" +
                "Com uma interface de linha de comando simples e eficiente, você pode facilmente visualizar,\n" +
                "analisar e atualizar seu portfólio de ativos financeiros.\n\n" +
                "Características principais:\n" +
                "  - Monitoramento de ativos em tempo real\n" +
                "  - Análises detalhadas de desempenho\n" +
                "  - Suporte para múltiplos portfólios\n\n" +
                "Estamos felizes em tê-lo conosco, e esperamos que o Nebula ajude você a alcançar seus objetivos financeiros.\n\n" +
                "Vamos começar?\n";

        // Exibir a apresentação com estilização
        ConsoleUtils.printWithTypingEffect(new AttributedString(
                introduction,
                AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)).toAnsi());

        String ajuda1 = "Comece digitando ";
        String ajuda2 = "help";
        String ajuda3 = " para ver a lista de comandos disponíveis.";

        System.out.print(new AttributedString(
                ajuda1,
                AttributedStyle.DEFAULT.foreground(CommandUtils.ColorExtensions.GRAY)).toAnsi());

        System.out.print(new AttributedString(
                ajuda2,
                AttributedStyle.BOLD.foreground(AttributedStyle.BLACK).background(AttributedStyle.GREEN)).toAnsi());

        ConsoleUtils.printWithTypingEffect(new AttributedString(
                ajuda3,
                AttributedStyle.DEFAULT.foreground(CommandUtils.ColorExtensions.GRAY)).toAnsi());
    }
}
