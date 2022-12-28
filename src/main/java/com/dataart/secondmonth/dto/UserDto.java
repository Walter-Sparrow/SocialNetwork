package com.dataart.secondmonth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    @NotNull
    public Long id;

    @Size(min = 4, max = 25)
    public String username;

    @Size(max = 255)
    public String email;

    public ImageDto profilePicture;

    @Pattern(regexp = "^$|[a-zA-Z]{2,50}", message = "First name is invalid.")
    public String firstName;

    @Pattern(regexp = "^$|[a-zA-Z]{2,50}", message = "Middle name is invalid.")
    public String middleName;

    @Pattern(regexp = "^$|[a-zA-Z]{2,50}", message = "Last name is invalid.")
    public String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate birthday;

    public AuthProviderDto authProvider;

}
