package dev.pethaven.controllers;

import dev.pethaven.dto.ChatDTO;
import dev.pethaven.dto.MessageDTO;
import dev.pethaven.entity.Pet;
import dev.pethaven.exception.NotFoundException;
import dev.pethaven.pojo.LoginRequest;
import dev.pethaven.pojo.MessageResponse;
import dev.pethaven.repositories.*;
import dev.pethaven.services.ChatService;
import dev.pethaven.services.MessageService;
import kotlin._Assertions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    ChatRepository chatRepository;

    @GetMapping(value = "/chats")
    public List<ChatDTO> getChats(Principal principal) {
        return chatService.getAllChats(principal);
    }

    @GetMapping(value = "/chats/messages/{id}")
    public List<MessageDTO> getMessages(@PathVariable("id") @NotNull(message = "Id cannot be null") Long chatId) {
        return messageService.getAllMessagesByChat(chatId);
    }

    @PostMapping(value = "/chats/messages")
    public ResponseEntity<?> addMessage(@RequestBody MessageDTO messageDTO) {
        messageService.addMessage(messageDTO);
        return ResponseEntity.ok().body(new MessageResponse("Message added"));
    }

    @PostMapping(value = "/chats")
    public ResponseEntity<?> createChatAndAddRequestMessage(@RequestParam("orgUsername") String orgUsername,
                                                            @RequestParam("userUsername") String userUsername,
                                                            @RequestParam("petId") Long petId) {
        boolean isChatExists = chatRepository.existsByOrganizationAuthUsernameAndUserAuthUsername(orgUsername, userUsername);
        if (!isChatExists) {
            chatService.createChat(orgUsername, userUsername);
        }
        messageService.addRequestMessage(petId, orgUsername, userUsername);
        return ResponseEntity.ok().body(new MessageResponse("RequestMessage sent"));
    }
}
