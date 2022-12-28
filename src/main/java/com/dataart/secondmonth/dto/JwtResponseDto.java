package com.dataart.secondmonth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JwtResponseDto {

    private String token;

    private final String type = "Bearer";

    private List<String> roles;

}
