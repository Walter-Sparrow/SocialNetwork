package com.dataart.secondmonth.persistence.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

public interface GroupPostProjection {

    Long getId();

    String getText();

    @Value("#{target.user_id}")
    Long getUserId();

    String getUserLogin();

    @Value("#{target.group_id}")
    Long getGroupId();

    String getGroupName();

    @Value("#{target.created_at}")
    Instant getCreatedAt();

    @Value("#{target.islike}")
    Boolean getLikedByAuthorized();

    Integer getLikesCount();

    Integer getDislikesCount();

}
