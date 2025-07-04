package com.masterovit08;

import java.util.logging.*;

public class ServerLogger {
    private static final Logger logger = Logger.getLogger(ServerLogger.class.getName());

    public static void info(String message){
        logger.log(Level.INFO, message + "\n");
    }

    public static void warning(String message){
        logger.log(Level.WARNING, message + "\n");
    }

    public static void error(String message){
        logger.log(Level.SEVERE, message + "\n");
    }

    public static void fine(String message){
        logger.log(Level.FINE, message + "\n");
    }
}
