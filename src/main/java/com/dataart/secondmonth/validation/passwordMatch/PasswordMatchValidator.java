package com.dataart.secondmonth.validation.passwordMatch;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {

    private String password;
    private String confirmPassword;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        password = constraintAnnotation.password();
        confirmPassword = constraintAnnotation.confirmPassword();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        Object passwordValue = new BeanWrapperImpl(o)
                .getPropertyValue(password);
        Object confirmPasswordValue = new BeanWrapperImpl(o)
                .getPropertyValue(confirmPassword);

        if (passwordValue != null) {
            return passwordValue.equals(confirmPasswordValue);
        } else {
            return confirmPasswordValue == null;
        }
    }

}
