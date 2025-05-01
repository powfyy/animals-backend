package dev.animals.service.animal;

import dev.animals.entity.OrganizationEntity;
import dev.animals.entity.UserEntity;
import dev.animals.entity.animal.AnimalAttributeValueEntity;
import dev.animals.entity.animal.AnimalEntity;
import dev.animals.entity.animal.AnimalPhotosEntity;
import dev.animals.entity.animal.AnimalTypeEntity;
import dev.animals.entity.pk.animal.AnimalAttributeValuePK;
import dev.animals.enums.AnimalStatus;
import dev.animals.exception.LogicException;
import dev.animals.exception.helper.CommonErrorCode;
import dev.animals.mapper.UserMapper;
import dev.animals.mapper.animal.AnimalMapper;
import dev.animals.repository.animal.AnimalRepository;
import dev.animals.repository.specification.PetSpecification;
import dev.animals.service.OrganizationService;
import dev.animals.service.UserService;
import dev.animals.web.dto.AnimalFilterDto;
import dev.animals.web.dto.UserDto;
import dev.animals.web.dto.animal.AnimalDto;
import dev.animals.web.dto.animal.AnimalSaveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimalService {

  private final AnimalRepository repository;
  private final AnimalTypeService typeService;
  private final AnimalPhotoService photoService;
  private final OrganizationService organizationService;
  private final UserService userService;

  /**
   * Получение животных
   *
   * @param page номер страницы
   * @param size размер страницы
   * @return отфильтрованный список
   */
  public Page<AnimalDto> getAll(int page, int size) {
    return repository.findAll(PageRequest.of(page, size))
      .map(AnimalMapper.MAPPER::toDto);
  }

  /**
   * Получение животных с фильтрацией
   *
   * @param page         номер страницы
   * @param size         размер страницы
   * @param filterFields объект с полями фильтрации
   * @return отфильтрованный список
   */
  public Page<AnimalDto> getAllFiltered(int page, int size, AnimalFilterDto filterFields) {
    return repository.findAll(new PetSpecification(filterFields), PageRequest.of(page, size))
      .map(AnimalMapper.MAPPER::toDto);
  }

  /**
   * Получение всех животных организации по ее логину
   *
   * @param username логин организации
   * @return список животных {@link AnimalDto}
   */
  public List<AnimalDto> getAllByOrganizationUsername(String username) {
    return AnimalMapper.MAPPER.toDtoList(repository.findByOrganizationUsername(username));
  }

  /**
   * Получение животного по id
   *
   * @param id id животного
   * @return {@link AnimalDto} объект животного
   */
  public AnimalDto getById(Long id) {
    if (Objects.isNull(id)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно получить животное: переданный id равен null");
    }
    return AnimalMapper.MAPPER.toDto(findById(id));
  }

  /**
   * Получение сущности животного по id
   *
   * @param id id животного
   * @return сущность животного
   */
  public AnimalEntity findById(Long id) {
    if (Objects.isNull(id)) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Невозможно найти животного по id: переданный id равен null");
    }
    return repository.findById(id)
      .orElseThrow(() -> new LogicException(CommonErrorCode.COMMON_OBJECT_NOT_EXISTS,
        "Невозможно получить животное: не найдено животное с id: " + id));
  }

  /**
   * Создание животного
   *
   * @param dto dto животного
   * @return созданное животное
   */
  @Transactional
  public AnimalDto create(AnimalSaveDto dto) {
    if (Objects.isNull(dto)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно создать животное: переданный dto равен null");
    }
    OrganizationEntity organization = organizationService.findByUsername(dto.getOrganizationUsername());
    AnimalTypeEntity type = typeService.findByName(dto.getType());
    dto.getAttributes().entrySet().stream()
      .filter(entry -> type.getAttributes().stream()
        .noneMatch(attribute ->
          attribute.getId().getAttributeName().equalsIgnoreCase(entry.getKey()) && attribute.getId().getAttributeValue().equalsIgnoreCase(entry.getValue())))
      .findAny()
      .ifPresent(attr -> {
        throw new LogicException(
          CommonErrorCode.VALIDATION_ERROR,
          String.format("Невозможно создать животное: передан некорректный атрибут для данного вида. Атрибут: %s, значение: %s", attr.getKey(), attr.getValue())
        );
      });
    AnimalEntity animal = AnimalMapper.MAPPER.toEntity(dto, organization, type);
    repository.save(animal);
    animal.getAttributeValues().addAll(dto.getAttributes().entrySet().stream()
      .map(entry -> new AnimalAttributeValueEntity(
        new AnimalAttributeValuePK(animal.getId(), type.getName(), entry.getKey().toLowerCase(), entry.getValue().toLowerCase())
      ))
      .toList());
    repository.save(animal);
    return AnimalMapper.MAPPER.toDto(animal);
  }

  /**
   * Метод обновления данных животного
   *
   * @param dto dto с обновленными данными
   * @return обновленное животное
   */
  @Transactional
  public AnimalDto update(AnimalSaveDto dto) {
    if (Objects.isNull(dto) || Objects.isNull(dto.getId())) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможного обновить данные животного: переданный dto или id равен null");
    }
    AnimalEntity savedAnimal = findById(dto.getId());
    if (savedAnimal.getStatus().equals(AnimalStatus.ADOPTED)) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Невозможно обновить данные животного: нельзя обновлять данные усыновленного животного");
    }
    AnimalTypeEntity type = typeService.findByName(dto.getType());
    dto.getAttributes().entrySet().stream()
      .filter(entry -> type.getAttributes().stream()
        .noneMatch(attribute ->
          attribute.getId().getAttributeName().equalsIgnoreCase(entry.getKey()) && attribute.getId().getAttributeValue().equalsIgnoreCase(entry.getValue())))
      .findAny()
      .ifPresent(attr -> {
        throw new LogicException(
          CommonErrorCode.VALIDATION_ERROR,
          String.format("Невозможно обновить животное: передан некорректный атрибут для данного вида. Атрибут: %s, значение: %s", attr.getKey(), attr.getValue())
        );
      });
    if (!savedAnimal.getType().getName().equals(dto.getType())) {
      savedAnimal.setType(type);
    }
    updateStatus(savedAnimal, dto);
    if (!savedAnimal.getOrganization().getAuth().getUsername().equals(dto.getOrganizationUsername())) {
      savedAnimal.setOrganization(organizationService.findByUsername(dto.getOrganizationUsername()));
    }
    AnimalMapper.MAPPER.update(dto, savedAnimal);
    return AnimalMapper.MAPPER.toDto(repository.save(savedAnimal));
  }

  private void updateStatus(AnimalEntity animal, AnimalSaveDto dto) {
    if (Objects.isNull(dto.getStatus())) {
      return;
    }
    if (!animal.getStatus().canTransitionTo(dto.getStatus())) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR,
        String.format("Невозможно обновить статус животного: нельзя перевести животное в статус %s из %s",
          dto.getStatus(), animal.getStatus())
      );
    }
    if (dto.getStatus().equals(AnimalStatus.ADOPTED)) {
      animal.setUser(userService.findByUsername(dto.getUserUsername()));
      animal.getUserSet().clear();
    }
  }

  /**
   * Удаление животного по id
   *
   * @param id id животного
   */
  @Transactional
  public void delete(Long id) {
    if (Objects.isNull(id)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно удалить животное: переданный id равен null");
    }
    AnimalEntity animal = findById(id);
    photoService.removeBucket(animal.getAnimalPhotos().stream()
      .map(AnimalPhotosEntity::getPhotoRef)
      .toList(), animal.getId().toString());
    repository.deleteById(id);
  }

  /**
   * Сохранение фото животного
   *
   * @param id id животного
   * @param file фото
   */
  public void savePhoto(Long id, MultipartFile file) {
    if (Objects.isNull(id) || Objects.isNull(file)) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Невозможно сохранить фото: переданный id или file равен null");
    }
    photoService.save(findById(id), file);
  }

  /**
   * Удаление фото животного
   *
   * @param id id животного
   * @param photoRef ссылка на фото
   */
  public void removePhoto(Long id, String photoRef) {
    if (Objects.isNull(id) || StringUtils.isBlank(photoRef)) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR,
        String.format("Невозможно удалить фото: передан пустой параметр: id=%s, photoRef=%s", id, photoRef));
    }
    photoService.remove(id.toString(), photoRef);
  }

  /**
   * Удаление запроса пользователя
   *
   * @param animalId id животного
   * @param username логин пользователя
   */
  @Transactional //todo пересмотреть метод. Возможно можно сделать через список пользователей у животного
  public void deleteRequestUser(Long animalId, String username) {
    if (Objects.isNull(animalId) || StringUtils.isBlank(username)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно удалить пользовательский запрос: передан пустой параметр");
    }
    UserEntity user = userService.findByUsername(username);
    user.getAnimalSet().remove(findById(animalId));
    userService.save(user);
  }

  /**
   * Проверка существует ли запрос на животное
   *
   * @param username логин пользователя
   * @param animalId id животного
   * @return //todo пересмотреть возвращаемое значение, возможно ориентироваться на статус ответа запроса
   */
  public Map<String, Boolean> checkRequest(String username, Long animalId) {
    if (Objects.isNull(animalId) || StringUtils.isBlank(username)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно проверить запросы на животных: передан пустой параметр");
    }
    UserEntity user = userService.findByUsername(username);
    if (user.getAnimalSet().contains(findById(animalId))) {
      return Collections.singletonMap("isThereRequest", true);
    }
    return Collections.singletonMap("isThereRequest", false);
  }

  /**
   * Получение всех запросов на животное
   *
   * @param animalId id животного
   * @return Список пользователей
   */
  public Set<UserDto> getUserRequests(Long animalId) {
    if (Objects.isNull(animalId)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно получить пользовательский запрос: переданный id равен null");
    }
    return UserMapper.MAPPER.toDtoSet(findById(animalId).getUserSet());
  }

  /**
   * Создание запроса на животное
   *
   * @param username логин пользователя
   * @param animalId id животного
   */
  public void requestForAnimal(String username, Long animalId) {
    if (StringUtils.isBlank(username) || Objects.isNull(animalId)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно создать запрос на животного: передан пустой параметр");
    }
    UserEntity currentUser = userService.findByUsername(username);
    currentUser.getAnimalSet().add(findById(animalId));
    userService.save(currentUser);
  }
}
