package com.dataart.secondmonth.persistence.repository;

import com.dataart.secondmonth.persistence.entity.DirectMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DirectMessageRepository extends JpaRepository<DirectMessage, Long> {

    Long countByFromIdAndPeerId(Long fromId, Long peerId);

    Page<DirectMessage> findByPeerIdAndFromId(Long peerId, Long fromId, Pageable pageable);

    @Query(
            value = "select * " +
                    "from direct_message " +
                    "where (peer_id = :peerId and from_id = :fromId) or (peer_id = :fromId and from_id = :peerId)",
            countQuery = "select count(*) " +
                    "from direct_message " +
                    "where (peer_id = :peerId and from_id = :fromId) or (peer_id = :fromId and from_id = :peerId)",
            nativeQuery = true)
    Page<DirectMessage> findConversationByPeerIdAndFromId(Long peerId, Long fromId, Pageable pageable);

    Page<DirectMessage> findByChatRoomId(Long chatRoomId, Pageable pageable);

}
