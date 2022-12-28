package com.dataart.secondmonth.dto;

import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeavePostCommentDto {

    private Long replyId;

    private Long userId;

    private Long postId;

    private String text;

}
