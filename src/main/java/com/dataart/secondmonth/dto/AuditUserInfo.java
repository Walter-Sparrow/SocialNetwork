package com.dataart.secondmonth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditUserInfo {

    public Long id;

    public String username;

    public String email;

    public String firstName;

    public String middleName;

    public String lastName;

    public LocalDate birthday;

    public AuthProviderDto authProvider;

}
