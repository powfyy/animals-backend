package dev.animals.service.animal;

import dev.animals.entity.animal.AnimalTypeEntity;
import dev.animals.entity.attribute.AttributeValueEntity;
import dev.animals.entity.pk.animal.AttributeValuePK;
import dev.animals.exception.LogicException;
import dev.animals.exception.helper.CommonErrorCode;
import dev.animals.mapper.animal.AnimalTypeMapper;
import dev.animals.repository.animal.AnimalRepository;
import dev.animals.repository.animal.AnimalTypeRepository;
import dev.animals.service.AttributeService;
import dev.animals.web.dto.animal.AnimalTypeDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimalTypeService {

  private final AnimalTypeRepository repository;
  private final AttributeService attributeService;
  private final AnimalRepository animalRepository;

  /**
   * Получение всех видов животных
   *
   * @param page номер страницы
   * @param size размер страницы
   * @return список типов
   */
  public Page<AnimalTypeDto> getAll(int page, int size) {
    PageRequest pageable = PageRequest.of(page, size, Sort.by(AnimalTypeEntity.Fields.priority).ascending());
    return AnimalTypeMapper.INSTANCE.toDtoPage(repository.findAll(pageable));
  }

  /**
   * Сохранение видов животных
   *
   * @param dto дто
   */
  public void save(AnimalTypeDto dto) {
    if (Objects.isNull(dto)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно сохранить вид животного: переданный dto равен null");
    }
    dto.setAttributes(dto.getAttributes().entrySet().stream()
      .collect(Collectors.toMap(
        entry -> entry.getKey().toLowerCase(),
        entry -> entry.getValue().stream()
          .map(String::toLowerCase)
          .collect(Collectors.toSet())
      )));
    Set<AttributeValuePK> attributeValueIds = dto.getAttributes().entrySet().stream()
      .flatMap(entry -> entry.getValue().stream()
        .map(value -> new AttributeValuePK(value, entry.getKey())))
      .collect(Collectors.toSet());
    List<AttributeValueEntity> attributeValues = attributeService.findAllValuesByIds(attributeValueIds);
    if (attributeValues.size() != attributeValueIds.size()) {
      throw new LogicException(CommonErrorCode.COMMON_OBJECT_NOT_EXISTS,
        "Невозможно сохранить тип животного: не найдены следующие атрибуты: " + attributeValueIds.stream()
          .filter(id -> attributeValues.stream()
            .noneMatch(value -> Objects.equals(value.getId(), id)))
          .map(id -> String.join(" - ", id.getAttributeName(), id.getValue()))
          .collect(Collectors.joining(", ")));
    }
    repository.save(AnimalTypeMapper.INSTANCE.update(
      dto,
      repository.findByNameIgnoreCase(dto.getName())
        .map(savedType -> {
          savedType.getAttributes().clear();
          return savedType;
        })
        .orElse(new AnimalTypeEntity())
    ));
  }

  public void updatePriorities(List<AnimalTypeDto> dtos) {
    if (Objects.isNull(dtos) || dtos.isEmpty()) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR,
        "Невозможно обновить приоритеты видов животных: список вида пуст или null");
    }
    List<String> names = dtos.stream()
      .map(AnimalTypeDto::getName)
      .collect(Collectors.toList());
    List<AnimalTypeEntity> entities = repository.findAllById(names);
    Map<String, AnimalTypeDto> dtoMap = dtos.stream()
      .collect(Collectors.toMap(AnimalTypeDto::getName, dto -> dto));
    repository.saveAll(entities.stream()
      .peek(entity -> {
        AnimalTypeDto dto = dtoMap.get(entity.getName());
        if (Objects.nonNull(dto)) {
          entity.setPriority(dto.getPriority());
        }
      })
      .toList()
    );
  }

  /**
   * Удаление вида по названию
   *
   * @param name название вида
   */
  public void delete(String name) {
    if (StringUtils.isBlank(name)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно удалить вид животного: не передано название");
    }
    if (!repository.existsById(name)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно удалить вид животного: не найден вид с названием: " + name);
    }
    if (animalRepository.existsByTypeName(name)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно удалить вид животного: существуют животные этим видом");
    }
    repository.deleteById(name);
  }

  public AnimalTypeEntity findByName(String name) {
    return repository.findByNameIgnoreCase(name)
      .orElseThrow(() -> new LogicException(CommonErrorCode.COMMON_OBJECT_NOT_EXISTS,
        "Невозможно получить вид животного по названию: не найден вид с названием: " + name));
  }
}
