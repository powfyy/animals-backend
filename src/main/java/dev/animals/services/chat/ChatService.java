package dev.animals.services.chat;

import dev.animals.dto.ChatDto;
import dev.animals.entity.ChatEntity;
import dev.animals.exception.LogicException;
import dev.animals.exception.helper.CommonErrorCode;
import dev.animals.mappers.ChatMapper;
import dev.animals.repositories.ChatRepository;
import dev.animals.services.OrganizationService;
import dev.animals.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final ChatRepository chatRepository;
  private final OrganizationService organizationService;
  private final UserService userService;

  /**
   * Получение чатов пользователя
   *
   * @param page     номер страницы
   * @param size     размер страницы
   * @param username логин пользователя
   * @return отсортированный список чатов
   */
  public Page<ChatDto> getAll(int page, int size, String username) {
    return chatRepository.findChatsByUsername(
        username,
        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateLastMessage"))) //todo сделать сортировку без поля dateLastMessage
      .map(ChatMapper.MAPPER::toDto);
  }

  /**
   * Создание чата
   *
   * @param organizationUsername логин организации
   * @param userUsername         логин пользователя
   * @return созданный чат
   */
  public ChatEntity create(String organizationUsername,
                           String userUsername) {
    if (StringUtils.isBlank(userUsername) || StringUtils.isBlank(organizationUsername)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно создать чат: передан пустой логин");
    }
    return chatRepository.findChatByUsernames(organizationUsername, userUsername)
      .orElseGet(() -> chatRepository.save(new ChatEntity(
        organizationService.findByUsername(organizationUsername),
        userService.findByUsername(userUsername)
      )));
  }

  /**
   * Получение чата по логинам пользователей
   *
   * @param orgUsername  логин организации
   * @param userUsername логин пользователя
   * @return чат
   */
  public ChatEntity findByUsernames(String orgUsername, String userUsername) {
    if (StringUtils.isBlank(userUsername) || StringUtils.isBlank(orgUsername)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно создать чат: передан пустой логин");
    }
    return chatRepository.findChatByUsernames(orgUsername, userUsername)
      .orElseThrow(() -> new LogicException(CommonErrorCode.COMMON_OBJECT_NOT_EXISTS,
        "Не найден чат для: " + orgUsername + " и " + userUsername));
  }

  /**
   * Получение чата по id
   *
   * @param id id чата
   * @return чат
   */
  public ChatEntity findById(Long id) {
    return chatRepository.findById(id)
      .orElseThrow(() -> new LogicException(CommonErrorCode.COMMON_OBJECT_NOT_EXISTS, "Не найден чат с id" + id));
  }

  /**
   * Проверка является ли пользователь участником чата
   *
   * @param chatId   id проверяемого чата
   * @param username логин проверяемого пользователя
   * @return true, если является участником
   */
  public boolean isMember(Long chatId, String username) {
    if (Objects.isNull(chatId) || StringUtils.isBlank(username)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно проверить участника чата: переданный пустой параметр");
    }
    return chatRepository.isMember(chatId, username);
  }
}
