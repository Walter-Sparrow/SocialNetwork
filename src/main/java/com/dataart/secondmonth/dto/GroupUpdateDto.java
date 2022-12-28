package com.dataart.secondmonth.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class GroupUpdateDto {

    private Long id;

    @Size(max = 100)
    @NotNull
    @NotBlank
    private String name;

    @Size(max = 100)
    private String status;

    @Size(max = 1000)
    private String description;

    private Long pictureId;

}
