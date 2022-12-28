package com.dataart.secondmonth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageDto {

    private int pageNumber;

    private int pageSize;

    private Sort.Direction sortDirection;

    private String sortBy;

    public Pageable getPageable() {
        return PageRequest.of(
                this.pageNumber,
                this.pageSize,
                this.sortDirection,
                this.sortBy
        );
    }

}
