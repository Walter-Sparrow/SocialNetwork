package com.dataart.secondmonth.component;

import com.dataart.secondmonth.dto.PageDto;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Data
@Component
public class DefaultPaginationProperties {

    @Value("${pagination.defaultPageNumber}")
    private int pageNumber;

    @Value("${pagination.defaultPageSize}")
    private int pageSize;

    @Value("${pagination.defaultSortDirection}")
    private Sort.Direction sortDirection;

    @Value("${pagination.defaultSortBy}")
    private String sortBy;

    public PageDto getPageDto() {
        return PageDto.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .sortDirection(sortDirection)
                .sortBy(sortBy)
                .build();
    }

}
