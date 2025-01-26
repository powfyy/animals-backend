package dev.animals.service.chat;

import dev.animals.entity.ChatEntity;
import dev.animals.entity.MessageEntity;
import dev.animals.entity.animal.AnimalEntity;
import dev.animals.exception.LogicException;
import dev.animals.exception.helper.CommonErrorCode;
import dev.animals.mapper.MessageMapper;
import dev.animals.repository.MessageRepository;
import dev.animals.service.animal.AnimalService;
import dev.animals.web.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@Validated
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;
  private final AnimalService animalService;
  private final ChatService chatService;

  /**
   * Получение сообщений по id чата
   *
   * @param chatId   id чата
   * @param page     номер страницы
   * @param size     размер страницы
   * @param username логин пользователя
   * @return отсортированный список сообщений
   */
  public Page<MessageDto> getAllByChatId(Long chatId, int page, int size, String username) {
    if (!chatService.isMember(chatId, username)) {
      throw new LogicException(CommonErrorCode.AUTH_FAIL, "Чат недоступен этому пользователю");
    }
    return messageRepository.findAllByChatId(chatId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date")))
      .map(MessageMapper.MAPPER::toDto);
  }

  /**
   * Метод создания сообщения
   *
   * @param dto      объект с данными сообщения
   * @param username логин отправителя
   */
  @Transactional
  public void create(MessageDto dto, String username) {
    if (!chatService.isMember(dto.getChatId(), username)) {
      throw new LogicException(CommonErrorCode.AUTH_FAIL, "Чат недоступен этому пользователю");
    }
    ChatEntity chat = chatService.findById(dto.getChatId());
    MessageEntity newMessage = new MessageEntity(dto.getMessage(), chat);
    if (Objects.isNull(dto.getUserUsername())) {
      newMessage.setUser(chat.getUser());
    } else {
      newMessage.setOrganization(chat.getOrganization());
    }
    newMessage.setChat(chat);
    messageRepository.save(newMessage);
  }

  /**
   * Создание сообщения при создании запроса на животное
   *
   * @param animalId id животного
   * @param chat     сущность чата
   */
  public void createRequestMessage(Long animalId, ChatEntity chat) {
    if (Objects.isNull(animalId) || Objects.isNull(chat)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно создать сообщение с запросом: передан null в параметрах");
    }
    AnimalEntity animal = animalService.findById(animalId);
    MessageEntity message = new MessageEntity(
      "Заявка на животное с кличкой " + animal.getName() + ". Ссылка на питомца: http://localhost:4200/home/" + animal.getId(),
      chat
    );
    message.setUser(chat.getUser());
    message.setChat(chat);
    messageRepository.save(message);
  }
}
