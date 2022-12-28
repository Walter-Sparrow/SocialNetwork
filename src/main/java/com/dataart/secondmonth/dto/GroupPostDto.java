package com.dataart.secondmonth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupPostDto {

    private Long id;

    private UserDto user;

    private GroupDto group;

    @Size(max = 1000)
    private String text;

    private ZonedDateTime createdAt;

    private LinkDto linkAttachment;

    private Boolean likedByAuthorized;

    private List<ImageDto> images;

    private List<TopLayerPostCommentDto> comments;

    @Min(0)
    private Integer likesCount = 0;

    @Min(0)
    private Integer dislikesCount = 0;

}
