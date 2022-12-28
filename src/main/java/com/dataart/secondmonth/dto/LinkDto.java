package com.dataart.secondmonth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkDto {

    private Long id;

    private String domain;

    private String url;

    private String title;

    private String desc;

    private String image;

    private String imageAlt;

}
