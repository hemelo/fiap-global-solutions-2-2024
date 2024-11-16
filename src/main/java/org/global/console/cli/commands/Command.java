package org.global.console.cli.commands;

import org.global.console.utils.CommandUtils;
import org.jline.builtins.Completers;
import org.jline.reader.impl.completer.StringsCompleter;

import java.util.List;

public interface Command {
    void execute(String args[]);

    default String getCommand() {
        return CommandUtils.getCommandName(getClass());
    };

    String getDescription();
    String getSyntax();
    String getOptions();
    List<String> getExamples();

    default Completers.TreeCompleter.Node getCompleter() {
        return Completers.TreeCompleter.node(getCommand(), "");
    }
}
