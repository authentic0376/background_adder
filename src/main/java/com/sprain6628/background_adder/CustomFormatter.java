package com.sprain6628.background_adder;

import java.util.logging.*;

public class CustomFormatter extends Formatter {
    // ANSI 색상 코드
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";      // SEVERE (빨간색)
    private static final String YELLOW = "\u001B[33m";   // WARNING (노란색)
    private static final String CYAN = "\u001B[36m";     // INFO (청록색)
    private static final String GREEN = "\u001B[32m";    // FINE (초록색)

    @Override
    public String format(LogRecord record) {
        String fullClassName = record.getSourceClassName();
        String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf(".") + 1); // 마지막 '.' 뒤가 클래스명

        String color = switch (record.getLevel().getName()) {
            case "SEVERE" -> RED;
            case "WARNING" -> YELLOW;
            case "INFO" -> CYAN;
            case "FINE" -> GREEN;
            default -> RESET;
        };

        return String.format("%1$s[%2$tM:%2$tS] [%3$s] [%4$s] :::: %5$s %6$s%n",
                color,                      // ANSI 색상 코드
                record.getMillis(),         // 시간 (분:초)
                record.getLevel(),          // 로그 레벨 (INFO, WARNING 등)
                simpleClassName + "." + record.getSourceMethodName(), // 클래스명.메소드명
                record.getMessage(),
                RESET);                     // 색상 초기화
    }
}

