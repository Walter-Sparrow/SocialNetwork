package com.dataart.secondmonth.service;

import com.dataart.secondmonth.dto.DirectMessageDto;
import com.dataart.secondmonth.dto.DirectMessageToSendDto;
import com.dataart.secondmonth.dto.PageDto;
import com.dataart.secondmonth.persistence.entity.ChatRoom;
import com.dataart.secondmonth.persistence.entity.DirectMessage;
import com.dataart.secondmonth.persistence.entity.User;
import com.dataart.secondmonth.persistence.repository.ChatRoomRepository;
import com.dataart.secondmonth.persistence.repository.DirectMessageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class DirectMessageService {

    private final DirectMessageRepository directMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    private final ModelMapper mapper;

    public DirectMessageDto save(
            DirectMessageToSendDto toSendDto,
            UsernamePasswordAuthenticationToken authenticationToken) {
        final var from = (User) (authenticationToken).getPrincipal();

        final var peer = new User();
        peer.setId(userService.getById(toSendDto.getPeerId()).id);

        final var chatRoom = chatRoomRepository
                .findByParticipant1IdAndParticipant2Id(from.getId(), peer.getId())
                .orElseGet(() ->
                        chatRoomRepository.save(
                                ChatRoom.builder()
                                        .participant1(from)
                                        .participant2(peer)
                                        .build()
                        )
                );

        final var message = DirectMessage.builder()
                .chatRoom(chatRoom)
                .from(from)
                .peer(peer)
                .text(toSendDto.getText())
                .date(ZonedDateTime.now())
                .build();

        return mapper.map(directMessageRepository.save(message), DirectMessageDto.class);
    }

    public Page<DirectMessageDto> findMessagesForAuthUser(Long participantId, PageDto pageDto) {
        final var authUserId = userService.getAuthorisedUser().id;

        return directMessageRepository
                .findConversationByPeerIdAndFromId(participantId, authUserId, pageDto.getPageable())
                .map(m -> mapper.map(m, DirectMessageDto.class));
    }

}
