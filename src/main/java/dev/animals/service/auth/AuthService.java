package dev.animals.service.auth;

import dev.animals.entity.AuthEntity;
import dev.animals.enums.Role;
import dev.animals.exception.LogicException;
import dev.animals.exception.helper.CommonErrorCode;
import dev.animals.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService { //todo почистить неиспользуемые методы

  private final AuthRepository authRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthEntity findByUsername(String username) {
    return authRepository.findByUsername(username)
      .orElseThrow(() -> new LogicException(CommonErrorCode.COMMON_OBJECT_NOT_EXISTS,
        "Не найдены регистрационные данные с логином: " + username));
  }

  public AuthEntity createAuth(String username, Role role, String password) {
    return authRepository.save(new AuthEntity(username, role, passwordEncoder.encode(password)));
  }

  public boolean existsByUsername(String username) {
    return authRepository.existsByUsername(username);
  }
}
