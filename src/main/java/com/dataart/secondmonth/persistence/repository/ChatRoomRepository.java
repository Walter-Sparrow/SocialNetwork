package com.dataart.secondmonth.persistence.repository;

import com.dataart.secondmonth.persistence.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query(
            value = "select * " +
                    "from chat_room " +
                    "where participant_id_1=:participantId or participant_id_2=:participantId",
            countQuery = "select count(*) " +
                    "from chat_room " +
                    "where participant_id_1=:participantId or participant_id_2=:participantId",
            nativeQuery = true)
    Page<ChatRoom> findAllByParticipant(Long participantId, Pageable pageable);

    @Query(
            value = "select * " +
                    "from chat_room " +
                    "where (participant_id_1=:participant1Id and participant_id_2=:participant2Id) or " +
                    "(participant_id_1=:participant2Id and participant_id_2=:participant1Id)",
            nativeQuery = true)
    Optional<ChatRoom> findByParticipant1IdAndParticipant2Id(Long participant1Id, Long participant2Id);

}
