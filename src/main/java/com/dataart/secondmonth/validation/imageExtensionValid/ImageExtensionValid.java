package com.dataart.secondmonth.validation.imageExtensionValid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE_USE, ElementType.PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy= ImageExtensionValidator.class)
@Documented
public @interface ImageExtensionValid {

    String message() default "File is not an image";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
