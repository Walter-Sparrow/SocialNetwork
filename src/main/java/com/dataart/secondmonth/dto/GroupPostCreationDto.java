package com.dataart.secondmonth.dto;

import com.dataart.secondmonth.validation.groupPostCreationValid.GroupPostCreationValid;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@GroupPostCreationValid
public class GroupPostCreationDto {

    @Size(max = 1000)
    private String text;

    private List<Long> imageIds;

    private LinkDto link;

}
