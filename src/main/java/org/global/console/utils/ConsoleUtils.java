package org.global.console.utils;

import org.apache.commons.lang3.StringUtils;
import org.global.console.cli.widgets.SkipWidget;
import org.jline.reader.Widget;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.jline.widget.Widgets;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utilidade para impressão de texto no console
 */
public class ConsoleUtils {

    public static final AtomicBoolean skipText = new AtomicBoolean(false);

    public static void printWithTypingEffect(String text) {

        if (StringUtils.isBlank(text)) return;

        if (skipText.get()) skipText.set(false);

        for (char c : text.toCharArray()) {

            if (skipText.get()) {
                System.out.print(c);
                continue;
            }

            System.out.print(c);

            try {
                Thread.sleep(3);
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

    public static void printStyledWarning(String s) {
        printWithTypingEffect(new AttributedString(
                s,
                AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).toAnsi());
    }

    public static String getWidgetName(Class<? extends Widgets> widget) {
        return widget.getSimpleName();
    }
}
