package org.global.console.cli.commands;

import org.global.console.utils.CommandUtils;
import org.global.console.utils.ConsoleUtils;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;

public class SysInfoCommand implements Command {
    @Override
    public void execute(String[] args) {
        printTitle("System Information");

        // Display host and OS details
        CommandUtils.printDetail("Host Name", getHostName());
        CommandUtils.printDetail("Operating System", System.getProperty("os.name"));
        CommandUtils.printDetail("OS Version", System.getProperty("os.version"));
        CommandUtils.printDetail("Architecture", System.getProperty("os.arch"));

        // Java and JVM details
        CommandUtils.printDetail("Java Version", System.getProperty("java.version"));
        CommandUtils.printDetail("Java Vendor", System.getProperty("java.vendor"));
        CommandUtils.printDetail("JVM Version", System.getProperty("java.vm.version"));
        CommandUtils.printDetail("JVM Name", System.getProperty("java.vm.name"));

        // CPU details
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        CommandUtils.printDetail("Available Processors", String.valueOf(osBean.getAvailableProcessors()));

        // Memory details
        long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024); // in MB
        long freeMemory = Runtime.getRuntime().freeMemory() / (1024 * 1024);   // in MB
        long maxMemory = Runtime.getRuntime().maxMemory() / (1024 * 1024);     // in MB
        CommandUtils.printDetail("Total Memory", totalMemory + " MB");
        CommandUtils.printDetail("Free Memory", freeMemory + " MB");
        CommandUtils.printDetail("Max Memory", maxMemory + " MB");

        // System uptime
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000; // in seconds
        CommandUtils.printDetail("System Uptime", uptime + " seconds");

        // User details
        CommandUtils.printDetail("User Name", System.getProperty("user.name"));
        CommandUtils.printDetail("User Home Dir", System.getProperty("user.home"));

        // Display current date and time

        printTitle("Current Datetime");

        ConsoleUtils.printWithTypingEffect(new AttributedString(LocalDateTime.now().toString(),
                AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).toAnsi());
    }

    private void printTitle(String title) {
        ConsoleUtils.printWithTypingEffect(new AttributedString("\n=== " + title + " ===",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)).toAnsi());
    }

    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Unknown Host";
        }
    }

    @Override
    public String getDescription() {
        return "Retorna informações sobre o sistema operacional e a máquina virtual Java.";
    }

    @Override
    public String getSyntax() {
        return CommandUtils.createSyntax(getCommand(), null, null);
    }

    @Override
    public List<String> getExamples() {
        return List.of(getCommand());
    }
}
