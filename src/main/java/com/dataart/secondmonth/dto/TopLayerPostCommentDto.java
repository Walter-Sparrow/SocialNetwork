package com.dataart.secondmonth.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopLayerPostCommentDto {

    private Long id;

    private Long repliesCount = 0L;

    private PostCommentDto firstReply;

    private UserDto user;

    private GroupPostDto post;

    @Size(min = 1, max = 1000)
    private String text;

    private ZonedDateTime createdAt;

}
