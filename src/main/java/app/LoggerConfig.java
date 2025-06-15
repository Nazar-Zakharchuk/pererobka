package app;

import java.io.IOException;
import java.util.logging.*;

public class LoggerConfig {

    private static final Logger logger = Logger.getLogger("AppLogger");

    public static Logger getLogger() {
        return logger;
    }

    public static void setup() {
        try {
            // Пишемо лог у файл
            FileHandler fileHandler = new FileHandler("logs/app.log", true);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);

            // Додатково виводимо в консоль
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            logger.addHandler(consoleHandler);

        } catch (IOException e) {
            System.err.println("Не вдалося налаштувати логгер: " + e.getMessage());
        }
    }
}
