package com.dataart.secondmonth.dto;

import com.dataart.secondmonth.validation.passwordMatch.PasswordMatch;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@PasswordMatch(
        password = "password",
        confirmPassword = "confirmPassword"
)
public class ChangePasswordDto {

    @NotNull
    public Long userId;

    @NotBlank
    @NotNull
    public String oldPassword;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
    public String password;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
    public String confirmPassword;

}
