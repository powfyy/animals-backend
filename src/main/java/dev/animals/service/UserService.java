package dev.animals.service;

import dev.animals.entity.AuthEntity;
import dev.animals.entity.UserEntity;
import dev.animals.enums.Role;
import dev.animals.exception.LogicException;
import dev.animals.exception.helper.CommonErrorCode;
import dev.animals.mapper.UserMapper;
import dev.animals.repository.UserRepository;
import dev.animals.service.auth.AuthService;
import dev.animals.web.dto.SignupUserRequest;
import dev.animals.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final AuthService authService;
  private final PasswordEncoder passwordEncoder;

  /**
   * Получение данных пользователя по логину
   *
   * @param username логин пользователя
   * @return данные о пользователя
   */
  public UserDto getByUsername(String username) {
    return UserMapper.MAPPER.toDto(findByUsername(username));
  }

  /**
   * Создание пользователя
   *
   * @param request объект с регистрационными данными
   * @return созданный пользователь
   */
  public UserDto create(SignupUserRequest request) {
    if (Objects.isNull(request)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно зарегистрировать пользователя: переданный dto равен null");
    }
    if (authService.existsByUsername(request.getUsername())) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно зарегистрировать пользователя: логин уже занят");
    }
    UserEntity user = new UserEntity(request.getName(), request.getLastname(), request.getPhoneNumber());
    user.setAuth(new AuthEntity(request.getUsername(), Role.USER, passwordEncoder.encode(request.getPassword())));
    return UserMapper.MAPPER.toDto(save(user));
  }

  /**
   * Обновление пользователя
   *
   * @param dto обновленные данные пользователя
   * @return обновленный пользователь
   */
  public UserDto update(UserDto dto) {
    if (Objects.isNull(dto)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно обновить пользователя: переданный dto равен null");
    }
    UserEntity user = findByUsername(dto.getUsername());
    UserMapper.MAPPER.updateUser(dto, user);
    save(user);
    return UserMapper.MAPPER.toDto(user);
  }

  /**
   * Удаление пользователя по логину
   *
   * @param username логин пользователя
   */
  @Transactional
  public void deleteByUsername(String username) {
    if (StringUtils.isBlank(username)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно удалить пользователя: переданный логин пуст");
    }
    userRepository.deleteByAuthUsername(username);
  }

  /**
   * Получение сущности пользователя по логину
   *
   * @param username логин пользователя
   * @return сущность пользователя
   */
  public UserEntity findByUsername(String username) {
    return userRepository.findByAuthUsername(username)
      .orElseThrow(() -> new LogicException(CommonErrorCode.COMMON_OBJECT_NOT_EXISTS,
        "Невозможно получить пользователя: не найден пользователь с логином: " + username));
  }

  public List<UserEntity> findAllByUsernames(List<String> usernames) {
    if (Objects.isNull(usernames)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно получить пользователя: передан пустой список логинов");
    }
    return userRepository.findAllByAuthUsernameIn(usernames);

  }

  /**
   * Сохранение пользователя в БД
   *
   * @param user сущность пользователя
   * @return сохраненная сущность
   */
  public UserEntity save(UserEntity user) {
    return userRepository.save(user);
  }
}
