package com.dataart.secondmonth.persistence.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_room")
public class ChatRoom {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "participant_id_1", nullable = false)
    private User participant1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "participant_id_2", nullable = false)
    private User participant2;

}
