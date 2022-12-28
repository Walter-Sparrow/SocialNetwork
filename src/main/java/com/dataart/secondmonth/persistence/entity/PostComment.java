package com.dataart.secondmonth.persistence.entity;

import com.dataart.secondmonth.dto.PostCommentDto;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post_comment")
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private GroupPost post;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private PostComment parent;

    @OneToOne
    @JoinColumn(name = "reply_id")
    private PostComment reply;

    @Column(name = "text")
    private String text;

    @Column(name = "createdat")
    private ZonedDateTime createdAt;

}
