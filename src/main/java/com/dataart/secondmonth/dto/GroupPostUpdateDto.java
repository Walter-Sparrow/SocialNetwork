package com.dataart.secondmonth.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class GroupPostUpdateDto {

    private Long id;

    @Size(max = 1000)
    private String text;

    private List<Long> imageIds;

    private LinkDto link;

}
