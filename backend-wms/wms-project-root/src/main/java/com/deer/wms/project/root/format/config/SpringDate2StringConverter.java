package com.deer.wms.project.root.format.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.convert.converter.Converter;

import lombok.RequiredArgsConstructor;

/**
 * Spring的Date转String的Converter
 * 
 * @author xuesinuo
 *
 */
@DependsOn("localDateTimeFormatter")
@RequiredArgsConstructor
@Configuration
public class SpringDate2StringConverter {

    private final FormatterConfig formatterConfig;

    private final LocalDateTimeFormatter formatter;

    @Bean
    Converter<Date, String> date2StringConverter() {
        return new Converter<Date, String>() {
            @Override
            public String convert(Date date) {
                SimpleDateFormat sdf = new SimpleDateFormat(formatterConfig.getDatetimePattern());
                return sdf.format(date);
            }
        };
    }

    @Bean
    Converter<String, Date> string2DateConverter() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String str) {
                SimpleDateFormat sdf = new SimpleDateFormat(formatterConfig.getDatetimePattern());
                try {
                    return sdf.parse(str);
                } catch (ParseException e) {
                }
                sdf = new SimpleDateFormat(formatterConfig.getDatePattern());
                try {
                    return sdf.parse(str);
                } catch (ParseException e) {
                }
                return null;
            }
        };
    }

    @Bean
    Converter<LocalDateTime, String> localDateTime2StringConverter() {
        return new Converter<LocalDateTime, String>() {
            @Override
            public String convert(LocalDateTime localDateTime) {
                return formatter.dateTime().format(localDateTime);
            }
        };
    }

    @Bean
    Converter<String, LocalDateTime> string2LocalDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String str) {
                return LocalDateTime.parse(str, formatter.date());
            }
        };
    }

    @Bean
    Converter<LocalDate, String> localDate2StringConverter() {
        return new Converter<LocalDate, String>() {
            @Override
            public String convert(LocalDate localDate) {
                return formatter.date().format(localDate);
            }
        };
    }

    @Bean
    Converter<String, LocalDate> string2LocalDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String str) {
                return LocalDate.parse(str, formatter.date());
            }
        };
    }

    @Bean
    Converter<LocalTime, String> localTime2StringConverter() {
        return new Converter<LocalTime, String>() {
            @Override
            public String convert(LocalTime localTime) {
                return formatter.time().format(localTime);
            }
        };
    }

    @Bean
    Converter<String, LocalTime> string2LocalTimeConverter() {
        return new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String str) {
                return LocalTime.parse(str, formatter.date());
            }
        };
    }
}
