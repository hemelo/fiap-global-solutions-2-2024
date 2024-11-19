package org.global.console.threads;

import org.global.console.infra.DataSource;
import org.global.console.utils.ConsoleUtils;

public class DataSourceCheckerThread extends Thread {

    public static DataSourceCheckerThread dataSourceCheckerThread;

    private DataSourceCheckerThread() {
        super("DataSourceCheckerThread");
    }

    public static DataSourceCheckerThread initialize() {

        if (dataSourceCheckerThread != null) {
            return dataSourceCheckerThread;
        }

        dataSourceCheckerThread = new DataSourceCheckerThread();
        dataSourceCheckerThread.start();
        return dataSourceCheckerThread;
    }

    @Override
    public void run() {

        Thread thread;

        while (true) {

            if (!DataSource.getInstance().isInitializing.get()) continue;

            thread = new Thread(() -> {
                synchronized (DataSource.getInstance().isInitializing) {
                    while (DataSource.getInstance().isInitializing.get()) {
                        try {
                            DataSource.getInstance().isInitializing.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                Thread.currentThread().interrupt();
            });

            thread.start();

            try {
                thread.join(10000);

                if (DataSource.getInstance().isInitializing.get()) {
                    ConsoleUtils.printStyledWarning("Atenção: O banco de dados está demorando a ser inicializado.");
                }
            } catch (Exception e) {
                if (DataSource.getInstance().isInitializing.get()) {
                    ConsoleUtils.printStyledWarning("Atenção: O banco de dados ainda está demorando a ser inicializado.");
                }
            }
        }

    }
}
