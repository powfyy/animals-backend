package dev.pethaven.services;

import dev.pethaven.dto.MessageDTO;
import dev.pethaven.entity.Chat;
import dev.pethaven.entity.Message;
import dev.pethaven.entity.Pet;
import dev.pethaven.exception.InvalidChatException;
import dev.pethaven.mappers.MessageMapper;
import dev.pethaven.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class MessageService {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    OrganizationService organizationService;
    @Autowired
    UserService userService;
    @Autowired
    PetService petService;
    @Autowired
    ChatService chatService;

    public List<MessageDTO> getAllMessagesByChat(Long chatId, Principal principal) {
        if (!chatService.isParticipant(chatId, principal.getName())) {
            throw new InvalidChatException("The messages of this chat are not available");
        }
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
                chatService.findById(messageDTO.getChatId()));
        if (messageDTO.getUserUsername() != null) {
            newMessage.setUser(userService.findByUsername(messageDTO.getUserUsername()));
        } else {
            newMessage.setOrganization(organizationService.findByUsername(messageDTO.getOrganizationUsername()));
        }
        messageRepository.save(newMessage);
    }

    public void addRequestMessage(@NotNull(message = "Pet's id can't be null") Long petId,
                                  @NotNull(message = "Chat can't be null") Chat chat) {

        Pet pet = petService.findById(petId);
        Message message = new Message(
                "Заявка на питомца с кличкой " + pet.getName() + ". Ссылка на питомца: http://localhost:4200/home/" + pet.getId(),
                LocalDateTime.now(),
                chat
        );
        message.setUser(chat.getUser());
        messageRepository.save(message);
    }
}
