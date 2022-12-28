package com.dataart.secondmonth.dto;

import lombok.Data;

@Data
public class UserReactionDto {

    private Long id;

    private UserDto user;

    private Long userId;

    private GroupPostDto post;

    private Long postId;

    private boolean isLike;

}
