package com.deer.wms.project.root.format.config;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import lombok.RequiredArgsConstructor;

/**
 * JDK8的LocalDateTime系列Json自动序列化
 * 
 * @author xuesinuo 2020-07-24
 */
@RequiredArgsConstructor
@Configuration
public class JsonMvcConfig {
    private final LocalDateTimeFormatter formatter;

    // ==========MvcResult + ObjectMapper使用Jackson2ObjectMapperBuilderCustomizer==========
    // ==========Date类型Result + ObjectMapper使用spring.jackson.date-format==========
    /**
     * 自定义Jackson2ObjectMapperBuilderCustomizer Bean<br>
     * springMCV转Json的日期格式<br>
     *
     * @return Spring序列化器
     */
    @Bean
    @Primary
    Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter.dateTime()))
                .serializerByType(LocalDate.class, new LocalDateSerializer(formatter.date()))
                .serializerByType(LocalTime.class, new LocalTimeSerializer(formatter.time()))
                .serializerByType(Number.class, new JsonSerializer<Number>() {
                    @Override
                    public void serialize(Number value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
                        gen.writeNumber(decimalFormat.format(value));
                    }
                })
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(formatter.dateTime()))
                .deserializerByType(LocalDate.class, new LocalDateDeserializer(formatter.date()))
                .deserializerByType(LocalTime.class, new LocalTimeDeserializer(formatter.time()))
        ;
    }
}