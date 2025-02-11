package com.sprain6628.background_adder.config;

import java.io.IOException;
import java.util.logging.LogManager;

public class LoggingConfig {
    public static void setupLogging() {
        try {
            LogManager.getLogManager().readConfiguration(
                    LoggingConfig.class.getResourceAsStream("/logging.properties")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
