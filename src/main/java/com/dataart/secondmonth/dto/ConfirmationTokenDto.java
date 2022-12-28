package com.dataart.secondmonth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmationTokenDto {

    private UUID token;

    private UserDto user;

    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();

}
