package com.deer.wms.project.root.format.config;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.context.annotation.DependsOn;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * JRE8 LocalDateTime 标准的手动格式化Formatter<br>
 * 
 * DateTimeFormatter线程安全<br>
 * 
 * 即使使用static属性，最好可能注入此Bean，确保static属性已经被赋值为可用格式<br>
 * 
 * @author xuesinuo 2020-07-30
 */
@Component
public class LocalDateTimeFormatter {
    private final DateTimeFormatter dateTime;
    private final DateTimeFormatter date;
    private final DateTimeFormatter time;

    public LocalDateTimeFormatter(FormatterConfig formatterConfig) {
        dateTime = DateTimeFormatter.ofPattern(formatterConfig.getDatetimePattern());
        date = DateTimeFormatter.ofPattern(formatterConfig.getDatePattern());
        time = DateTimeFormatter.ofPattern(formatterConfig.getTimePattern());
    }

    public DateTimeFormatter dateTime() {
        return dateTime;
    }

    public DateTimeFormatter date() {
        return date;
    }

    public DateTimeFormatter time() {
        return time;
    }

    public ZoneOffset zoneOffset() {
        return ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset() / 1000);
    }

    public Long getTimestamp(ChronoLocalDateTime<?> dateTime) {
        return Optional.ofNullable(dateTime)
                .map(x -> x.toInstant(zoneOffset()))
                .map(x -> x.toEpochMilli())
                .orElse(null);
    }

    public LocalDateTime getLocalDateTime(Long timestamp) {
        return Optional.ofNullable(timestamp)
                .map(x -> Instant.ofEpochMilli(x))
                .map(x -> LocalDateTime.ofInstant(x, zoneOffset()))
                .orElse(null);
    }
}

// ==========JRE8 LocalDateTimeFormatter implements spring.Formatter==========
// ==========HTTP请求入参Jackson转换优先级Formatter > Converter > @DateTimeFormat==========

@RequiredArgsConstructor
@Component
@DependsOn("localDateTimeFormatter")
class LocalDateTimeFormatterConfig implements Formatter<LocalDateTime> {
    private final LocalDateTimeFormatter localDateTimeFormatter;
    @Override
    public String print(LocalDateTime localDateTime, Locale locale) {
        return localDateTimeFormatter.dateTime().format(localDateTime);
    }

    @Override
    public LocalDateTime parse(String dateTimeString, Locale locale) {
        return LocalDateTime.parse(dateTimeString, localDateTimeFormatter.dateTime());
    }
}

@RequiredArgsConstructor
@Component
@DependsOn("localDateTimeFormatter")
class LocalDateFormatterConfig implements Formatter<LocalDate> {
    private final LocalDateTimeFormatter localDateTimeFormatter;
    @Override
    public String print(LocalDate localDate, Locale locale) {
        return localDateTimeFormatter.date().format(localDate);
    }

    @Override
    public LocalDate parse(String dateString, Locale locale) {
        return LocalDate.parse(dateString, localDateTimeFormatter.date());
    }
}

@RequiredArgsConstructor
@Component
@DependsOn("localDateTimeFormatter")
class LocalTimeFormatterConfig implements Formatter<LocalTime> {
    private final LocalDateTimeFormatter localDateTimeFormatter;
    @Override
    public String print(LocalTime localTime, Locale locale) {
        return localDateTimeFormatter.time().format(localTime);
    }

    @Override
    public LocalTime parse(String timeString, Locale locale) {
        return LocalTime.parse(timeString, localDateTimeFormatter.time());
    }
}