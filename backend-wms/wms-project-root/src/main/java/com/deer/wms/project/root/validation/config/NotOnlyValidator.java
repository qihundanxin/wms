package com.deer.wms.project.root.validation.config;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.deer.wms.project.root.validation.annotation.NotOnly;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotOnlyValidator implements ConstraintValidator<NotOnly, Object> {
    private NotOnly ann;

    @Override
    public void initialize(NotOnly methodValid) {
        this.ann = methodValid;
    }

    @Override
    public boolean isValid(Object target, ConstraintValidatorContext context) {
        if (target == null) {
            return ann.nullable();
        }
        String[] notOnlyStringArray = ann.value();
        Set<String> notOnlySet = new HashSet<>();
        notOnlySet.add("class");
        for (String notOnlyString : notOnlyStringArray) {
            notOnlySet.add(notOnlyString);
        }
        Class<?> targetClass = target.getClass();

        try {
            BeanInfo beanInfo = java.beans.Introspector.getBeanInfo(targetClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (notOnlySet.contains(propertyDescriptor.getName())) {
                    continue;
                }
                if (propertyDescriptor.getReadMethod() == null) {
                    continue;
                }
                Object invokeResult = propertyDescriptor.getReadMethod().invoke(target);
                if (invokeResult != null) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("JSR303-@NotOnly出现反射获取属性异常", e);
            throw new RuntimeException(e);
        }
        return false;
    }

}
