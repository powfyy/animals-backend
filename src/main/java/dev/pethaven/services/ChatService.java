package dev.pethaven.services;

import dev.pethaven.dto.ChatDTO;
import dev.pethaven.entity.*;
import dev.pethaven.exception.NotFoundException;
import dev.pethaven.mappers.ChatMapper;
import dev.pethaven.repositories.AuthRepository;
import dev.pethaven.repositories.ChatRepository;
import dev.pethaven.repositories.OrganizationRepository;
import dev.pethaven.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@Service
@Validated
public class ChatService {
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    AuthRepository authRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatMapper chatMapper;

    public List<ChatDTO> getAllChats(Principal user) {
        Auth auth = authRepository.findByUsername(user.getName())
                .orElseThrow(() -> new NotFoundException("Current user not found"));

        if (auth.getRole() == Role.ORG) {
            List<Chat> chats = chatRepository.findByOrganizationId(organizationRepository.findByAuthId(auth.getId())
                    .orElseThrow(() -> new NotFoundException("Organization not found")).getId());
            return chatMapper.toDtoList(chats);
        }

        List<Chat> chats = chatRepository.findByUserId(userRepository.findByAuthId(auth.getId())
                .orElseThrow(() -> new NotFoundException("User not found")).getId());
        return chatMapper.toDtoList(chats);
    }

    public void createChat(@NotNull(message = "Organization's username can't be null") String organizationUsername,
                           @NotNull(message = "User's username can't be null") String userUsername) {
        User user = userRepository.findByAuthId(
                authRepository.findByUsername(userUsername)
                        .orElseThrow(() -> new NotFoundException("Auth is not found"))
                        .getId()
                )
                .orElseThrow(() -> new NotFoundException("User is not found"));
        Organization organization = organizationRepository.findByAuthId(
                authRepository.findByUsername(organizationUsername)
                        .orElseThrow(() -> new NotFoundException("Auth is not found"))
                        .getId())
                .orElseThrow(() -> new NotFoundException("Organization is not found"));
        Chat chat = new Chat(null, user, organization);
        chatRepository.save(chat);
    }

    public Chat findChatByUserUsernameAndOrgUsername(String orgUsername, String userUsername) {
       return chatRepository.findByOrganizationAuthUsernameAndUserAuthUsername(orgUsername, userUsername)
               .orElseThrow(() -> new NotFoundException("Chat not found"));
    }

}
