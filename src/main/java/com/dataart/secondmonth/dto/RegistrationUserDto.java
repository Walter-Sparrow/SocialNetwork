package com.dataart.secondmonth.dto;

import com.dataart.secondmonth.validation.passwordMatch.PasswordMatch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@PasswordMatch(
        password = "password",
        confirmPassword = "confirmPassword"
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationUserDto {

    @NotBlank
    @Pattern(regexp = "^(?!.*\\.\\.)(?!.*\\.$)[^\\W][\\w.]{0,15}$", message = "Login is invalid.")
    public String username;

    @Email
    public String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
    public String password;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
    public String confirmPassword;

    @Pattern(regexp = "^$|[a-zA-Z]{2,50}", message = "First name is invalid.")
    public String firstName;

    @Pattern(regexp = "^$|[a-zA-Z]{2,50}", message = "Middle name is invalid.")
    public String middleName;

    @Pattern(regexp = "^$|[a-zA-Z]{2,50}", message = "Last name is invalid.")
    public String lastName;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    public LocalDate birthday;

}
