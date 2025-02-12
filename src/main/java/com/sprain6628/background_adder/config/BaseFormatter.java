package com.sprain6628.background_adder.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class BaseFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        String fullClassName = record.getSourceClassName();
        String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = record.getSourceMethodName();

        return String.format(
                "%1$s[%2$s] [%3$s] [%4$s] :::: %5$s %6$s%n"
                , prefix(record)
                , getFormattedTime(record.getMillis())
                , record.getLevel()
                , simpleClassName + "." + methodName
                , record.getMessage()
                , suffix()
        );
    }

    // 공통 시간 포맷 메서드
    private String getFormattedTime(long millis) {
        return new SimpleDateFormat(timeFormat()).format(new Date(millis));
    }

    protected String timeFormat() {
        return "yyyy-MM-dd HH:mm:ss.SSS";
    }

    protected String prefix(LogRecord record) {
        return "";
    }

    protected String suffix() {
        return "";
    }
}
