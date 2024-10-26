package dev.animals.services;

import dev.animals.dto.OrganizationDto;
import dev.animals.dto.OrganizationDtoCityName;
import dev.animals.dto.SignupOrganizationRequest;
import dev.animals.entity.AuthEntity;
import dev.animals.entity.OrganizationEntity;
import dev.animals.enums.Role;
import dev.animals.exception.LogicException;
import dev.animals.exception.helper.CommonErrorCode;
import dev.animals.mappers.OrganizationMapper;
import dev.animals.repositories.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Метод для работы с организациями
 */
@Service
@RequiredArgsConstructor
public class OrganizationService {

  private final OrganizationRepository organizationRepository;
  private final AuthService authService;
  private final PasswordEncoder passwordEncoder;

  /**
   * Получение организации по логину
   *
   * @param username логин
   * @return {@link OrganizationDto}
   */
  public OrganizationDto getByUsername(String username) {
    return OrganizationMapper.MAPPER.toDTO(findByUsername(username));
  }

  /**
   * Получение списка названий организаций с ее городом
   *
   * @return {@link OrganizationDtoCityName}
   */
  public List<OrganizationDtoCityName> getCityAndName() {
    return organizationRepository.findAll().stream()
      .map(OrganizationMapper.MAPPER::toDtoCityName)
      .collect(Collectors.toList());
  }

  /**
   * Метод создания организации
   *
   * @param request дто с регистрационными данными
   * @return созданная организация
   */
  public OrganizationDto create(SignupOrganizationRequest request) {
    if (Objects.isNull(request)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно зарегистрировать организацию: переданный dto равен null");
    }
    if (authService.existsByUsername(request.getUsername())) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно зарегистрировать организацию: логин занят");
    }
    OrganizationEntity organization = new OrganizationEntity(
      request.getNameOrganization(),
      request.getCity(),
      request.getPassportNumber(),
      request.getPassportSeries(),
      request.getPhoneNumber()
    );
    organization.setAuth(new AuthEntity(request.getUsername(), Role.ORG, passwordEncoder.encode(request.getPassword())));
    return OrganizationMapper.MAPPER.toDTO(organizationRepository.save(organization));
  }

  /**
   * Метод обновления данных об организации
   *
   * @param dto объект с новыми данными
   * @return обновленная организация
   */
  public OrganizationDto update(OrganizationDto dto) {
    if (Objects.isNull(dto)) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Невозможно обновить данные организации: переданный dto равен null");
    }
    OrganizationEntity organization = findByUsername(dto.getUsername());
    OrganizationMapper.MAPPER.updateOrganization(dto, organization);
    return OrganizationMapper.MAPPER.toDTO(organizationRepository.save(organization));
  }

  /**
   * Удаление по логину
   *
   * @param username логин
   */
  @Transactional
  public void deleteByUsername(String username) {
    organizationRepository.deleteByAuthUsername(username);
  }

  /**
   * Получение сущности организации по логину
   *
   * @param username логину
   * @return {@link OrganizationEntity} сущность организации
   */
  public OrganizationEntity findByUsername(String username) {
    return organizationRepository.findByAuthUsername(username)
      .orElseThrow(() -> new LogicException(CommonErrorCode.COMMON_OBJECT_NOT_EXISTS,
        "Невозможно получить организацию: не найдена организация с логином: " + username));
  }
}
