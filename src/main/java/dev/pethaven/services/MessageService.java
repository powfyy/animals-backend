package dev.pethaven.services;

import dev.pethaven.dto.MessageDTO;
import dev.pethaven.entity.Chat;
import dev.pethaven.entity.Message;
import dev.pethaven.entity.Pet;
import dev.pethaven.exception.InvalidChatException;
import dev.pethaven.mappers.MessageMapper;
import dev.pethaven.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public Page<MessageDTO> getMessagesByChat(Long chatId, int page, int size, Principal principal) {
        if (!chatService.isParticipant(chatId, principal.getName())) {
            throw new InvalidChatException("The messages of this chat are not available");
        }
        return messageRepository.findAllByChatId(chatId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"))).map(messageMapper::toDto);
    }

    @Transactional
    public void addMessage(@Valid MessageDTO messageDTO, Principal principal) {
        if (!chatService.isParticipant(messageDTO.getChatId(), principal.getName())) {
            throw new InvalidChatException("The messages of this chat are not available");
        }
        Chat chat = chatService.findById(messageDTO.getChatId());
        Message newMessage = new Message(null,
                messageDTO.getMessage(),
                LocalDateTime.parse(messageDTO.getDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        newMessage.setChat(chat);
        if (messageDTO.getUserUsername() != null) {
            newMessage.setUser(userService.findByUsername(messageDTO.getUserUsername()));
        } else {
            newMessage.setOrganization(organizationService.findByUsername(messageDTO.getOrganizationUsername()));
        }
        chat.setDateLastMessage(newMessage.getDate());
        messageRepository.save(newMessage);
    }

    public void addRequestMessage(@NotNull(message = "Pet's id can't be null") Long petId,
                                  @NotNull(message = "Chat can't be null") Chat chat) {

        Pet pet = petService.findById(petId);
        Message message = new Message(
                "Заявка на питомца с кличкой " + pet.getName() + ". Ссылка на питомца: http://localhost:4200/home/" + pet.getId(),
                LocalDateTime.now()
        );
        message.setChat(chat);
        message.setUser(chat.getUser());
        chat.setDateLastMessage(message.getDate());
        messageRepository.save(message);
    }
}
