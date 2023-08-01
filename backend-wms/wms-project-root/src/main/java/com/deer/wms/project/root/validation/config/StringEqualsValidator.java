package com.deer.wms.project.root.validation.config;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.deer.wms.project.root.validation.annotation.StringEquals;

/**
 * {@link StringEquals} 验证规则
 * 
 * @author xuesinuo 2021-1-27
 */
public class StringEqualsValidator implements ConstraintValidator<StringEquals, Object> {

    private StringEquals ann;

    @Override
    public void initialize(StringEquals ann) {
        this.ann = ann;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return ann.nullable();
        }
        for (String item : ann.value()) {
            if (value.toString().equals(item)) {
                return true;
            }
        }
        return false;
    }

}
