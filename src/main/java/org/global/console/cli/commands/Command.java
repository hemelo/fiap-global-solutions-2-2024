package org.global.console.cli.commands;

import org.global.console.utils.CommandUtils;

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
}
