package com.dataart.secondmonth.validation.groupPostCreationValid;

import com.dataart.secondmonth.dto.GroupPostCreationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GroupPostCreationValidator implements ConstraintValidator<GroupPostCreationValid, GroupPostCreationDto> {

    @Override
    public boolean isValid(GroupPostCreationDto post, ConstraintValidatorContext constraintValidatorContext) {
        return !((post.getText() == null || post.getText().isBlank()) && post.getImageIds() == null);
    }

}
