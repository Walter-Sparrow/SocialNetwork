package com.dataart.secondmonth.controller;

import com.dataart.secondmonth.component.DefaultPaginationProperties;
import com.dataart.secondmonth.dto.*;
import com.dataart.secondmonth.service.ChatRoomService;
import com.dataart.secondmonth.service.DirectMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final DirectMessageService messageService;
    private final ChatRoomService chatRoomService;

    private final DefaultPaginationProperties paginationProperties;

    @Value("${messenger.direct-chat.pagination.defaultPageSize}")
    private int defaultChatPageSize;

    @Value("${messenger.direct-chat.pagination.defaultSortBy}")
    private String defaultChatSortBy;

    @MessageMapping("/chat")
    public void processMessage(@Payload DirectMessageToSendDto toSendDto, UsernamePasswordAuthenticationToken authenticationToken) {
        final var message = messageService.save(toSendDto, authenticationToken);

        messagingTemplate.convertAndSendToUser(
                message.getPeer().id.toString(), "/queue/messages",
                message
        );
        messagingTemplate.convertAndSendToUser(
                message.getFrom().id.toString(), "/queue/messages",
                message
        );
    }

    @GetMapping("api/messages/chat/{participantId}")
    public ResponseEntity<Page<DirectMessageDto>> findMessages(
            @PathVariable Long participantId,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {
        PageDto conversationPage = paginationProperties.getPageDto();

        conversationPage.setPageNumber(pageNumber != null ? pageNumber : paginationProperties.getPageNumber());
        conversationPage.setSortBy(defaultChatSortBy);
        conversationPage.setPageSize(defaultChatPageSize);
        return ResponseEntity.ok(messageService.findMessagesForAuthUser(participantId, conversationPage));
    }

    @GetMapping("api/chats/self")
    public ResponseEntity<Page<UserDto>> findAllAuthUserChats(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {

        PageDto roomsPage = paginationProperties.getPageDto();
        roomsPage.setPageNumber(pageNumber != null ? pageNumber : paginationProperties.getPageNumber());

        return ResponseEntity.ok(chatRoomService.findAllAuthUserChats(roomsPage));
    }

    @GetMapping("api/chats/participant/{id}")
    public ResponseEntity<UserDto> getParticipantById(@PathVariable Long id) {
        return ResponseEntity.ok(chatRoomService.findByParticipantId(id));
    }

}
