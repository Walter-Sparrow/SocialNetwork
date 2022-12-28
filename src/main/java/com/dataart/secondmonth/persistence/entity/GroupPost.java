package com.dataart.secondmonth.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "group_post")
public class GroupPost {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(name = "text", nullable = false)
    private String text;

    @OneToOne
    @JoinColumn(name = "link_attachment_id")
    private Link linkAttachment;

    @OneToMany
    @JoinTable(
            name = "attachment",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private List<Image> images;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

}
