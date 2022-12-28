package com.dataart.secondmonth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {

    @NotNull
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String name;

    private ImageDto groupPicture;

    @Size(max = 500)
    private String description;

    @Size(max = 100)
    private String status;

    private ZonedDateTime createdAt;

    private UserDto owner;

}
