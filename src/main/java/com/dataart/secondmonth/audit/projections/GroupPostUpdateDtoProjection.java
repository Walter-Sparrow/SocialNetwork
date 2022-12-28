package com.dataart.secondmonth.audit.projections;

import java.util.List;

public interface GroupPostUpdateDtoProjection {

    String getText();

    LinkDtoProjection getLink();

    List<Long> getImageIds();

}
