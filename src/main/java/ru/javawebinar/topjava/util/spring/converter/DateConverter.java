package ru.javawebinar.topjava.util.spring.converter;


import org.springframework.core.convert.converter.Converter;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;

public final class DateConverter implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String s) {
        return DateTimeUtil.parseLocalDate(s);
    }
}
