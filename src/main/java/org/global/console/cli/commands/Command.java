package org.global.console.cli.commands;

import org.apache.commons.lang3.tuple.Pair;
import org.global.console.utils.CommandUtils;
import org.jline.builtins.Completers;

import java.util.List;

public interface Command {
    void execute(String args[]);

    default String getCommand() {
        return CommandUtils.getCommandName(getClass());
    };

    String getDescription();
    String getSyntax();
    List<String> getExamples();

    default Completers.TreeCompleter.Node getCompleter() {
        return Completers.TreeCompleter.node(getCommand(), "");
    }
}
