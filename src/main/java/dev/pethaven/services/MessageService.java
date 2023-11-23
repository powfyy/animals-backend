package dev.pethaven.services;

import dev.pethaven.dto.MessageDTO;
import dev.pethaven.entity.Message;
import dev.pethaven.entity.Pet;
import dev.pethaven.exception.NotFoundException;
import dev.pethaven.mappers.MessageMapper;
import dev.pethaven.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class MessageService {
    @Autowired
    MessageMapper messageMapper;

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    AuthRepository authRepository;
    @Autowired
    PetRepository petRepository;

    public List<MessageDTO> getAllMessagesByChat(Long chatId) {
        List<Message> messages = messageRepository.findAllByChatId(chatId);
        return messages.stream()
                .map(el -> messageMapper.toDto(el))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addMessage(@Valid MessageDTO messageDTO) {
        Message newMessage = new Message(null,
                messageDTO.getMessage(),
                LocalDateTime.parse(messageDTO.getDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
                chatRepository.findById(messageDTO.getChatId())
                        .orElseThrow(() -> new NotFoundException("Chat not found")));
        if (messageDTO.getUserUsername() != null) {
            newMessage.setUser(userRepository.findByAuthId(
                            authRepository.findByUsername(messageDTO.getUserUsername())
                                    .orElseThrow(() -> new NotFoundException("Auth not found")).getId())
                    .orElseThrow(() -> new NotFoundException("User not found")));
        } else {
            newMessage.setOrganization(organizationRepository.findByAuthId(
                            authRepository.findByUsername(messageDTO.getOrganizationUsername())
                                    .orElseThrow(() -> new NotFoundException("Auth not found")).getId())
                    .orElseThrow(() -> new NotFoundException("User not found")));
        }
        messageRepository.save(newMessage);
    }

    public void addRequestMessage(@NotNull(message = "Pet's id can't be null") Long petId,
                                  @NotNull(message = "Organization's username can't be null") String orgUsername,
                                  @NotNull(message = "User's username can't be null") String userUsername) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundException("Pet is not found"));
        Message message = new Message(
                null,
                "Заявка на питомца с кличкой " + pet.getName() + ". Ссылка на питомца: http://localhost:4200/home/"
                        + pet.getId(),
                LocalDateTime.now(),
                chatRepository.findByOrganizationAuthUsernameAndUserAuthUsername(orgUsername, userUsername)
                        .orElseThrow(() -> new NotFoundException("Chat is not found"))
        );
        message.setUser(userRepository.findByAuthId(
                        authRepository.findByUsername(userUsername)
                                .orElseThrow(() -> new NotFoundException("Auth not found")).getId())
                .orElseThrow(() -> new NotFoundException("User not found")));
        messageRepository.save(message);
    }
}
