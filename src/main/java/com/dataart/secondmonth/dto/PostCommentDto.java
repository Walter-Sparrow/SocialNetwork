package com.dataart.secondmonth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentDto {

    private Long id;

    private UserDto user;

    private GroupPostDto post;

    private PostCommentDto parent;

    private PostCommentDto reply;

    @Size(min = 1, max = 1000)
    private String text;

    private ZonedDateTime createdAt;

}
