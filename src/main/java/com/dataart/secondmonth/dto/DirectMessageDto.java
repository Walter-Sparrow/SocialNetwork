package com.dataart.secondmonth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DirectMessageDto {

    private Long id;

    private ChatRoomDto chatRoom;

    private UserDto peer;

    private UserDto from;

    private String text;

    private ZonedDateTime date;

}
