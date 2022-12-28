package com.dataart.secondmonth.service;

import com.dataart.secondmonth.dto.ChatRoomDto;
import com.dataart.secondmonth.dto.PageDto;
import com.dataart.secondmonth.dto.UserDto;
import com.dataart.secondmonth.exception.NotFoundException;
import com.dataart.secondmonth.persistence.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    private final ModelMapper mapper;

    public Page<ChatRoomDto> findAllByParticipantId(Long participantId, PageDto pageDto) {
        return chatRoomRepository.findAllByParticipant(participantId, pageDto.getPageable())
                .map(chatRoom -> mapper.map(chatRoom, ChatRoomDto.class));
    }

    public Page<UserDto> findAllAuthUserChats(PageDto pageDto) {
        final var authUser = userService.getAuthorisedUser();
        return chatRoomRepository.findAllByParticipant(authUser.id, pageDto.getPageable())
                .map(chatRoom ->
                        chatRoom.getParticipant1().getId().equals(authUser.id) ?
                                mapper.map(chatRoom.getParticipant2(), UserDto.class) :
                                mapper.map(chatRoom.getParticipant1(), UserDto.class));
    }

    public UserDto findByParticipantId(Long id) {
        final var authUser = userService.getAuthorisedUser();
        return chatRoomRepository.findByParticipant1IdAndParticipant2Id(id, authUser.id)
                .map(chatRoom ->
                        chatRoom.getParticipant1().getId().equals(authUser.id) ?
                                mapper.map(chatRoom.getParticipant2(), UserDto.class) :
                                mapper.map(chatRoom.getParticipant1(), UserDto.class))
                .orElseThrow(() ->
                        new NotFoundException("Auth user doesn't have a conversation with received participant"));
    }

}
