package org.global.console.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.global.console.Main;
import org.global.console.cli.commands.Command;
import org.jline.builtins.Completers;
import org.jline.reader.LineReader;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.jline.widget.Widgets;

import java.text.Normalizer;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utilidade para impressão de texto no console
 */
public class ConsoleUtils {

    public static final AtomicBoolean skipText = new AtomicBoolean(false);

    public static Completers.TreeCompleter getCompleter() {

        Pair<Set<Command>, Set<Class<? extends Command>>> commands = CommandUtils.getAllCommandsInstances();

        var completers = commands.getLeft().stream()
                .map(Command::getCompleter)
                .toList();

        return new Completers.TreeCompleter(completers);
    }

    public static void printWithTypingEffect(String text) {

        if (StringUtils.isBlank(text)) return;

        if (skipText.get()) skipText.set(false);

        text = Normalizer.normalize(text, Normalizer.Form.NFD) .replaceAll("\\p{M}", "");

        for (char c : text.toCharArray()) {

            if (skipText.get()) {
                System.out.print(c);
                continue;
            }

            System.out.print(c);

            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println();
    }

    public static void printStyledError(String error) {
        printWithTypingEffect(new AttributedString(
                error,
                AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)).toAnsi());
    }

    public static void printStyledWarning(String warn) {
        printWithTypingEffect(new AttributedString(
                warn,
                AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).toAnsi());
    }

    public static void printStyledSuccess(String success) {
        printWithTypingEffect(new AttributedString(
                success,
                AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).toAnsi());
    }

    public static boolean readBoolean() {
        LineReader reader = Main.getReader();
        List<String> trueTriggers = List.of("s", "y", "yes", "sim", "yep", "yeah", "sure", "claro", "ok", "okay", "true", "t", "1");
        String input = reader.readLine();

        if (input == null) return false;
        return trueTriggers.contains(input.toLowerCase().trim());
    }

    public static String getWidgetName(Class<? extends Widgets> widget) {
        return widget.getSimpleName();
    }
}
