package com.dataart.secondmonth.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "direct_message")
public class DirectMessage {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(optional = false)
    @JoinColumn(name = "peer_id", nullable = false)
    private User peer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "from_id", nullable = false)
    private User from;

    @Column(name = "text")
    private String text;

    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

}
