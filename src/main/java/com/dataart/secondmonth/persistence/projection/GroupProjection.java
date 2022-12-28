package com.dataart.secondmonth.persistence.projection;

import org.springframework.beans.factory.annotation.Value;

public interface GroupProjection {

    Long getId();

    String getName();

    @Value("#{target.membersCount}")
    Long getMembersCount();

}
