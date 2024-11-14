package org.global.console.cli.widgets;

import org.global.console.utils.ConsoleUtils;
import org.jline.keymap.KeyMap;
import org.jline.reader.LineReader;
import org.jline.reader.Reference;
import org.jline.widget.Widgets;
import static org.jline.keymap.KeyMap.ctrl;
import static org.jline.keymap.KeyMap.alt;

public class SkipWidget extends Widgets {

    public SkipWidget(LineReader reader) {
        super(reader);
        String name = ConsoleUtils.getWidgetName(SkipWidget.class);
        addWidget(name, this::run);
        getKeyMap().bind(new Reference(name), KeyMap.translate("\n"));
    }

    private boolean run() {
        ConsoleUtils.skipText.set(true);
        return true;
    }
}
