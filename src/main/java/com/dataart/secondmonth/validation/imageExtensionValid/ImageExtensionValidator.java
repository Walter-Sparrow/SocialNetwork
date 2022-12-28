package com.dataart.secondmonth.validation.imageExtensionValid;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ImageExtensionValidator implements ConstraintValidator<ImageExtensionValid, MultipartFile> {

    private Pattern fileTypeImageCheckRegex;

    @Override
    public void initialize(ImageExtensionValid annotation) {
        fileTypeImageCheckRegex = Pattern.compile("([^\\s]+(\\.(?i)(jpe?g|png|bmp|gif))$)");
    }

    @Override
    public boolean isValid(MultipartFile o, ConstraintValidatorContext constraintValidatorContext) {
        return o.getOriginalFilename() != null && fileTypeImageCheckRegex.matcher(o.getOriginalFilename()).find();
    }

}
