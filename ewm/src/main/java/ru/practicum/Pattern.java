package ru.practicum;

import java.time.format.DateTimeFormatter;

public class Pattern {
    public static final String DATE="yyyy-MM-dd HH:mm:ss";
    public  static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE);
}
