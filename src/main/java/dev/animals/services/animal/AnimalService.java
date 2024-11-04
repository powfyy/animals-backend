package dev.animals.services.animal;

import dev.animals.dto.AnimalDto;
import dev.animals.dto.AnimalSaveDto;
import dev.animals.dto.FilterFields;
import dev.animals.dto.UserDto;
import dev.animals.entity.UserEntity;
import dev.animals.entity.animal.AnimalEntity;
import dev.animals.entity.animal.AnimalPhotosEntity;
import dev.animals.enums.PetStatus;
import dev.animals.exception.LogicException;
import dev.animals.exception.helper.CommonErrorCode;
import dev.animals.mappers.AnimalMapper;
import dev.animals.mappers.UserMapper;
import dev.animals.repositories.AnimalPhotosRepository;
import dev.animals.repositories.AnimalRepository;
import dev.animals.services.MinioService;
import dev.animals.services.OrganizationService;
import dev.animals.services.UserService;
import dev.animals.specifications.PetSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimalService {

  private final AnimalRepository animalRepository;
  private final AnimalPhotosRepository animalPhotosRepository;
  private final OrganizationService organizationService;
  private final UserService userService;
  private final MinioService minioService;

  /**
   * Получение животных с фильтрацией
   *
   * @param page         номер страницы
   * @param size         размер страницы
   * @param filterFields объект с полями фильтрации
   * @return отфильтрованный список
   */
  public Page<AnimalDto> getAll(int page, int size, FilterFields filterFields) {
    return animalRepository.findAll(new PetSpecification(filterFields), PageRequest.of(page, size)).map(AnimalMapper.MAPPER::toDto);
  }

  /**
   * Получение всех животных организации по ее логину
   *
   * @param username логин организации
   * @return список животных {@link AnimalDto}
   */
  public List<AnimalDto> getAllByOrganizationUsername(String username) {
    return AnimalMapper.MAPPER.toDtoList(animalRepository.findByOrganizationUsername(username));
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
    return animalRepository.findById(id)
      .orElseThrow(() -> new LogicException(CommonErrorCode.COMMON_OBJECT_NOT_EXISTS,
        "Невозможно получить животное: не найдено животное с id: " + id));
  }

  /**
   * Создание животного
   *
   * @param organizationUsername логин организации
   * @param dto                  dto животного
   * @return созданное животное
   */
  @Transactional //todo пересмотреть метод
  public AnimalDto create(String organizationUsername, AnimalSaveDto dto) {
    AnimalEntity tempAnimal = AnimalMapper.MAPPER.toEntity(dto);
    AnimalEntity newAnimal = new AnimalEntity(
      tempAnimal.getName(),
      tempAnimal.getGender(),
      tempAnimal.getType(),
      tempAnimal.getBirthDay(),
      tempAnimal.getBreed(),
      tempAnimal.getDescription(),
      PetStatus.ACTIVE);
    newAnimal.setOrganization(organizationService.findByUsername(organizationUsername));
    animalRepository.save(newAnimal);
    String bucketName = newAnimal.getId().toString() + "-" + newAnimal.getType().toString().toLowerCase();
    minioService.createBucket(bucketName);
    if (!dto.getFiles().isEmpty()) {
      minioService.uploadFile(dto.getFiles(), bucketName);
      List<AnimalPhotosEntity> petPhotosList = new ArrayList<>();
      dto.getFiles().forEach(file -> {
        AnimalPhotosEntity petPhotos = new AnimalPhotosEntity(file.getOriginalFilename(), newAnimal);
        petPhotosList.add(petPhotos);
      });
      animalPhotosRepository.saveAll(petPhotosList);
    }
    return AnimalMapper.MAPPER.toDto(newAnimal);
  }

  /**
   * Метод обновления данных животного
   *
   * @param id  id животного
   * @param dto dto с обновленными данными
   * @return обновленное животное
   */
  @Transactional
  public AnimalDto update(Long id, AnimalSaveDto dto) {
    if (Objects.isNull(id) || Objects.isNull(dto)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможного обновить данные животного: передан null в параметрах");
    }
    String bucketName = id + "-" + dto.getTypePet().toLowerCase();
    AnimalEntity savedAnimal = findById(id);
    if (!dto.getDeletedPhotoRefs().isEmpty()) {
      minioService.removeFiles(dto.getDeletedPhotoRefs(), bucketName);
      dto.getDeletedPhotoRefs().forEach(animalPhotosRepository::deleteByPhotoRef);
    }
    if (!dto.getFiles().isEmpty()) {
      minioService.uploadFile(dto.getFiles(), bucketName);
      List<AnimalPhotosEntity> petPhotosList = new ArrayList<>();
      dto.getFiles().forEach(file -> petPhotosList.add(new AnimalPhotosEntity(file.getOriginalFilename(), savedAnimal)));
      animalPhotosRepository.saveAll(petPhotosList);
    }
    AnimalMapper.MAPPER.updatePet(dto, savedAnimal);
    return AnimalMapper.MAPPER.toDto(animalRepository.save(savedAnimal));
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
    String bucketName = animal.getId() + "-" + animal.getType().toString().toLowerCase();
    if (!animal.getAnimalPhotos().isEmpty()) {
      minioService.removeFiles(bucketName, animal.getAnimalPhotos());
    }
    minioService.removeBucket(bucketName);
    animalRepository.deleteById(id);
    animalPhotosRepository.deleteByAnimalId(id); //todo фотки должны удаляться каскадно
  }

  /**
   * Усыновление животного
   *
   * @param username логин пользователя
   * @param animalId id животного
   */
  @Transactional
  public void adoptPet(String username, Long animalId) {
    if (StringUtils.isBlank(username) || Objects.isNull(animalId)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно усыновить животное: передан пустой параметр");
    }
    AnimalEntity animal = findById(animalId);
    if (animal.getStatus() != PetStatus.FREEZE) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно усыновить животное: животное не заморожено");
    }
    UserEntity user = userService.findByUsername(username);
    animal.setUser(user);
    animal.getUserSet().clear();
    animal.setStatus(PetStatus.ADOPTED);
    animalRepository.save(animal);
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

  public void updateStatusPet(Long animalId, String newStatus) { //todo удалить метод. Сделать обновление статуса через обновление животного
    if (Objects.isNull(animalId) || StringUtils.isBlank(newStatus)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно обновить статус питомца: передан пустой параметр");
    }
    AnimalEntity updatedPet = findById(animalId);
    if (updatedPet.getStatus().canTransitionTo(PetStatus.valueOf(newStatus))) {
      updatedPet.setStatus(PetStatus.valueOf(newStatus));
      animalRepository.save(updatedPet);
    } else {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR,
        "Невозможно обновить статус питомца: текущий статус не может быть изменен на переданный");
    }
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
