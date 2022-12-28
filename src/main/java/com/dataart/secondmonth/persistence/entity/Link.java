package com.dataart.secondmonth.persistence.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "link")
public class Link {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "domain")
    private String domain;

    @Column(name = "url", nullable = false, unique = true)
    private String url;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String desc;

    @Column(name = "image")
    private String image;

    @Column(name = "imagealt")
    private String imageAlt;

}
