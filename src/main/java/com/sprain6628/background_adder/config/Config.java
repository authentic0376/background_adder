package com.sprain6628.background_adder.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.LogManager;

public class Config {
    private static final String DEFAULT_ENV = "dev";
    private static final String CONFIG_FILE = "/config.properties";
    private static final String LOG_DIR = "logs";

    public static void setupLogging() {
        String env = loadEnvironment();
        createLogDirectory();
        loadLoggingConfiguration(env);
    }

    private static String loadEnvironment() {
        Properties properties = new Properties();
        String env = DEFAULT_ENV;

        try (InputStream configStream = Config.class.getResourceAsStream(CONFIG_FILE)) {
            if (configStream != null) {
                properties.load(configStream);
                env = properties.getProperty("env", DEFAULT_ENV);
                System.out.println("env = " + env);
            } else {
                System.err.println("Can't find config.properties. Use default(dev)." );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return env;
    }

    private static void createLogDirectory() {
        File logDir = new File(LOG_DIR);
        if (!logDir.exists()) {
            boolean created = logDir.mkdirs();
            if (created) {
                System.out.println("📂 logs 디렉터리 생성됨.");
            } else {
                System.err.println("⚠️ logs 디렉터리 생성 실패!");
            }
        }
    }

    private static void loadLoggingConfiguration(String env) {
        String configFileName = String.format("/logging-%s.properties", env);

        try (InputStream loggingConfig = Config.class.getResourceAsStream(configFileName)) {
            if (loggingConfig != null) {
                LogManager.getLogManager().readConfiguration(loggingConfig);
                System.out.println("logging config loaded: " + configFileName);
            } else {
                System.err.println("Can't find logging config file: " + configFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
