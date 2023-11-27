package dev.pethaven.controllers;

import dev.pethaven.dto.ChatDTO;
import dev.pethaven.dto.MessageDTO;
import dev.pethaven.services.ChatService;
import dev.pethaven.services.MessageService;
import dev.pethaven.services.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(value = "/chats/messages/{id}")
    public List<MessageDTO> getMessages(@PathVariable("id") @NotNull(message = "Id cannot be null") Long chatId, Principal principal) {
        return messageService.getAllMessagesByChat(chatId, principal);
    }

    @PostMapping(value = "/chats/messages")
    public void addMessage(@RequestBody MessageDTO messageDTO) {
        messageService.addMessage(messageDTO);
    }

    @PostMapping(value = "/chats")
    public void createChatAndAddRequestMessage(@RequestParam("orgUsername") String orgUsername,
                                               @RequestParam("userUsername") String userUsername,
                                               @RequestParam("petId") Long petId) {
        messageService.addRequestMessage(petId, chatService.createChat(orgUsername, userUsername));
    }

    @PostMapping(value = "/{bucketName}")
    public void createBucket(@PathVariable("bucketName") String bucketName) {
        minioService.createBucket(bucketName);
    }
}
