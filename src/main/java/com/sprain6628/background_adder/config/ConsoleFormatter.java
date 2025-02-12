package com.sprain6628.background_adder.config;

import java.util.logging.LogRecord;

public class ConsoleFormatter extends BaseFormatter {
    // ANSI 색상 코드
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";      // SEVERE (빨간색)
    private static final String YELLOW = "\u001B[33m";   // WARNING (노란색)
    private static final String CYAN = "\u001B[36m";     // INFO (청록색)
    private static final String GREEN = "\u001B[32m";    // FINE (초록색)

    @Override
    protected String prefix(LogRecord record) {

        return switch (record.getLevel().getName()) {
            case "SEVERE" -> RED;
            case "WARNING" -> YELLOW;
            case "INFO" -> CYAN;
            case "FINE" -> GREEN;
            default -> RESET;
        };
    }

    @Override
    protected String suffix() {
        return RESET;
    }

    @Override
    protected String timeFormat() {

        return "mm:ss";
    }
}

