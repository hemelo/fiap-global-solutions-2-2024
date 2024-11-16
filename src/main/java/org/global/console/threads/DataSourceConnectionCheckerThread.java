package org.global.console.threads;

import org.global.console.infra.DataSource;
import org.global.console.utils.ConsoleUtils;

public class DataSourceConnectionCheckerThread extends Thread {

    public static DataSourceConnectionCheckerThread dataSourceCheckerThread;

    private DataSourceConnectionCheckerThread() {
        super("DataSourceConnectionCheckerThread");
    }

    public static DataSourceConnectionCheckerThread initialize() {

        if (dataSourceCheckerThread != null) {
            return dataSourceCheckerThread;
        }

        dataSourceCheckerThread = new DataSourceConnectionCheckerThread();
        dataSourceCheckerThread.start();
        return dataSourceCheckerThread;
    }

    @Override
    public void run() {

        Thread thread;

        while (true) {

            if (!DataSource.getInstance().isRetrievingConnection.get()) continue;

            thread = new Thread(() -> {
                synchronized (DataSource.getInstance().isRetrievingConnection) {
                    while (DataSource.getInstance().isRetrievingConnection.get()) {
                        try {
                            DataSource.getInstance().isRetrievingConnection.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

            thread.start();

            try {
                thread.join(10000);

                if (DataSource.getInstance().isRetrievingConnection.get()) {
                    ConsoleUtils.printStyledWarning("Atenção: O banco de dados está demorando para conectar.");
                }
            } catch (Exception e) {
                if (DataSource.getInstance().isRetrievingConnection.get()) {
                    ConsoleUtils.printStyledWarning("Atenção: O banco de dados está demorando para conectar.");
                }
            }
        }

    }
}
