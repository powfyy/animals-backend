package dev.pethaven.services;

import dev.pethaven.dto.ChatDTO;
import dev.pethaven.entity.*;
import dev.pethaven.enums.Role;
import dev.pethaven.exception.NotFoundException;
import dev.pethaven.mappers.ChatMapper;
import dev.pethaven.repositories.AuthRepository;
import dev.pethaven.repositories.ChatRepository;
import dev.pethaven.repositories.OrganizationRepository;
import dev.pethaven.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@Service
@Validated
public class ChatService {
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    OrganizationService organizationService;
    @Autowired
    UserService userService;
    @Autowired
    ChatMapper chatMapper;

    public Page<ChatDTO> getChats(Principal principal, int page, int size) {
        return chatRepository.findChatsByUsername(
                        principal.getName(),
                        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateLastMessage")))
                .map(chatMapper::toDTO);
    }

    public Chat createChat(@NotNull(message = "Organization's username can't be null") String organizationUsername,
                           @NotNull(message = "User's username can't be null") String userUsername) {
        return chatRepository.findChatByUsernames(organizationUsername, userUsername).orElseGet(() -> {
            Chat chat = new Chat();
            chat.setUser(userService.findByUsername(userUsername));
            chat.setOrganization(organizationService.findByUsername(organizationUsername));
            chatRepository.save(chat);
            return chat;
        });
    }

    public Chat findByUsernames(String orgUsername, String userUsername) {
        return chatRepository.findChatByUsernames(orgUsername, userUsername)
                .orElseThrow(() -> new NotFoundException("Chat not found"));
    }

    public Chat findById(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chat not found"));
    }

    public boolean isParticipant(Long chatId, String username) {
        return chatRepository.isParticipant(chatId, username);
    }
}
