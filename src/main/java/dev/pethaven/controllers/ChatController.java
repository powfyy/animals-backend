package dev.pethaven.controllers;

import dev.pethaven.dto.ChatDTO;
import dev.pethaven.dto.MessageDTO;
import dev.pethaven.services.ChatService;
import dev.pethaven.services.MessageService;
import dev.pethaven.services.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class ChatController {
    @Autowired
    ChatService chatService;
    @Autowired
    MessageService messageService;
    @Autowired
    MinioService minioService;


    @GetMapping(value = "/chats")
    public List<ChatDTO> getChats(Principal principal) {
        return chatService.getAllChats(principal);
    }

    @GetMapping(value = "/chats/messages/{id}/")
    public Page<MessageDTO> getMessages(@PathVariable("id") @NotNull(message = "Id cannot be null") Long chatId,
                                        @RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "35") int size,
                                        Principal principal) {
        return messageService.getMessagesByChat(chatId, page, size, principal);
    }

    @PostMapping(value = "/chats/messages")
    public void addMessage(@RequestBody MessageDTO messageDTO, Principal principal) {
        messageService.addMessage(messageDTO, principal);
    }

    @PostMapping(value = "/chats")
    public void createChatAndAddRequestMessage(@RequestParam("orgUsername") String orgUsername,
                                               @RequestParam("userUsername") String userUsername,
                                               @RequestParam("petId") Long petId) {
        messageService.addRequestMessage(petId, chatService.createChat(orgUsername, userUsername));
    }
}
