package com.dataart.secondmonth.persistence.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.ZonedDateTime;

public interface PostCommentProjection {

    Long getId();

    @Value("#{target.user_id}")
    Long getUserId();

    @Value("#{target.post_id}")
    Long getPostId();

    String getText();

    @Value("#{@zonedDateTimeAttributeConverter.convertToEntityAttribute(target.createdat)}")
    ZonedDateTime getCreatedAt();

    @Value("#{target.repliesCount}")
    Long getRepliesCount();

    @Value("#{target.first_reply}")
    Long getFirstReplyId();

}
