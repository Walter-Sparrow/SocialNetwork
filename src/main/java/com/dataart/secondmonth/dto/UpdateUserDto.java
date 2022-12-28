package com.dataart.secondmonth.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class UpdateUserDto {

    @NotNull
    public Long id;

    @Pattern(regexp = "^$|[a-zA-Z]{2,50}", message = "First name is invalid.")
    public String firstName;

    @Pattern(regexp = "^$|[a-zA-Z]{2,50}", message = "Middle name is invalid.")
    public String middleName;

    @Pattern(regexp = "^$|[a-zA-Z]{2,50}", message = "Last name is invalid.")
    public String lastName;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    public LocalDate birthday;

    public Long pictureId;

}
