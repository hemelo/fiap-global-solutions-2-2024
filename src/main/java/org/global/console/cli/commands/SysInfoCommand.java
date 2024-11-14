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
        printDetail("Host Name", getHostName());
        printDetail("Operating System", System.getProperty("os.name"));
        printDetail("OS Version", System.getProperty("os.version"));
        printDetail("Architecture", System.getProperty("os.arch"));

        // Java and JVM details
        printDetail("Java Version", System.getProperty("java.version"));
        printDetail("Java Vendor", System.getProperty("java.vendor"));
        printDetail("JVM Version", System.getProperty("java.vm.version"));
        printDetail("JVM Name", System.getProperty("java.vm.name"));

        // CPU details
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        printDetail("Available Processors", String.valueOf(osBean.getAvailableProcessors()));

        // Memory details
        long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024); // in MB
        long freeMemory = Runtime.getRuntime().freeMemory() / (1024 * 1024);   // in MB
        long maxMemory = Runtime.getRuntime().maxMemory() / (1024 * 1024);     // in MB
        printDetail("Total Memory", totalMemory + " MB");
        printDetail("Free Memory", freeMemory + " MB");
        printDetail("Max Memory", maxMemory + " MB");

        // System uptime
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000; // in seconds
        printDetail("System Uptime", uptime + " seconds");

        // User details
        printDetail("User Name", System.getProperty("user.name"));
        printDetail("User Home Dir", System.getProperty("user.home"));

        // Display current date and time

        printTitle("Current Datetime");

        ConsoleUtils.printWithTypingEffect(new AttributedString(LocalDateTime.now().toString(),
                AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).toAnsi());
    }

    private void printTitle(String title) {
        ConsoleUtils.printWithTypingEffect(new AttributedString("\n=== " + title + " ===",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)).toAnsi());
    }

    private void printDetail(String label, String value) {
        ConsoleUtils.printWithTypingEffect(new AttributedString(label + " : ",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).toAnsi() +
                new AttributedString(value,
                        AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).toAnsi());
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
        return "";
    }

    @Override
    public String getSyntax() {
        return "";
    }

    @Override
    public String getOptions() {
        return "";
    }

    @Override
    public List<String> getExamples() {
        return List.of();
    }
}
