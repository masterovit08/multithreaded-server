package com.masterovit08;

import java.util.logging.*;

class LogFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        return switch (record.getLevel().toString()) {
            case "INFO" -> "[\033[1;34m" + record.getLevel() + "\033[0m]: " + record.getMessage();
            case "WARNING" -> "[\033[1;33m" + record.getLevel() + "\033[0m]: " + record.getMessage();
            case "SEVERE" -> "[\033[1;31m" + "ERROR" + "\033[0m]: " + record.getMessage();
            default -> "";
        };
    }
}

public class ServerLogger {
    private static final Logger logger = Logger.getLogger(ServerLogger.class.getName());

    static{
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new LogFormatter());
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
    }

    public static void info(String message){
        logger.info(message + "\n");
    }

    public static void warning(String message){
        logger.warning(message + "\n");
    }

    public static void error(String message){
        logger.severe(message + "\n");
    }
}
