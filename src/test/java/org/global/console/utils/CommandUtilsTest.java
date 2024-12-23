package org.global.console.utils;

import org.global.console.cli.commands.Command;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CommandUtilsTest {

    @Test
    void testGetAllCommands() {
        assertDoesNotThrow(() -> {
            Set<Class<? extends Command>> commands = CommandUtils.getAllCommands();
            assertNotNull(commands);
        });
    }

    @Test
    void testGetCommandClass() {
        assertDoesNotThrow(() -> {
            Class<? extends Command> commandClass = CommandUtils.getCommandClass("help");
            assertNotNull(commandClass);
        });
    }

    @Test
    void testGetCommandInstanceByName() {
        assertDoesNotThrow(() -> {
            Command command = CommandUtils.getCommandInstance("help");
            assertNotNull(command);
        });
    }

    @Test
    void testGetCommandInstanceByClass() {
        assertDoesNotThrow(() -> {
            Class<? extends Command> commandClass = CommandUtils.getCommandClass("help");
            Command command = CommandUtils.getCommandInstance(commandClass);
            assertNotNull(command);
        });
    }

    @Test
    void testGetCommandName() {
        assertDoesNotThrow(() -> {
            Class<? extends Command> commandClass = CommandUtils.getCommandClass("help");
            String commandName = CommandUtils.getCommandName(commandClass);
            assertEquals("help", commandName);
        });
    }

    @Test
    void testGetAllCommandsInstances() {
        assertDoesNotThrow(() -> {
            var commandsInstances = CommandUtils.getAllCommandsInstances();
            assertNotNull(commandsInstances);
            assertNotNull(commandsInstances.getLeft());
            assertNotNull(commandsInstances.getRight());
        });
    }

    @Test
    void testCreateSyntax() {
        assertDoesNotThrow(() -> {
            String syntax = CommandUtils.createSyntax("test", List.of("option1"), List.of("option2"));
            assertNotNull(syntax);
        });

        assertDoesNotThrow(() -> {
            String syntax = CommandUtils.createSyntax("test", null, List.of("option2"));
            assertNotNull(syntax);
        });

        assertDoesNotThrow(() -> {
            String syntax = CommandUtils.createSyntax("test", null, null);
            assertNotNull(syntax);
        });
    }

    @Test
    void testLoadCommands() {
        assertDoesNotThrow(() -> {
            Map<String, Class<? extends Command>> commands = CommandUtils.loadCommands();
            assertNotNull(commands);
        });
    }
}