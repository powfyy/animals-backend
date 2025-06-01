package dev.animals.service;

import dev.animals.entity.attribute.AttributeEntity;
import dev.animals.entity.attribute.AttributeValueEntity;
import dev.animals.entity.pk.animal.AttributeValuePK;
import dev.animals.exception.LogicException;
import dev.animals.exception.helper.CommonErrorCode;
import dev.animals.mapper.AttributeMapper;
import dev.animals.repository.animal.AnimalTypeAttributeValueRepository;
import dev.animals.repository.attribute.AttributeRepository;
import dev.animals.repository.attribute.AttributeValueRepository;
import dev.animals.web.dto.AttributeDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttributeService {

  private final AttributeRepository repository;
  private final AttributeValueRepository valueRepository;
  private final AnimalTypeAttributeValueRepository animalTypeAttributeValueRepository;

  /**
   * Получение атрибутов
   *
   * @param page номер страницы
   * @param size размер страницы
   * @return список атрибутов
   */
  public Page<AttributeDto> getAll(int page, int size) {
    return AttributeMapper.INSTANCE.toDtoPage(repository.findAll(PageRequest.of(page, size, Sort.by(AttributeEntity.Fields.priority).ascending())));
  }

  /**
   * Получение атрибутов по названию
   *
   * @param name название атрибута
   * @return атрибут
   */
  public AttributeDto getByName(String name) {
    if (StringUtils.isBlank(name)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно получить атрибут по названию: передано пустое название");
    }
    return AttributeMapper.INSTANCE.toDto(findByName(name));
  }

  /**
   * Получение атрибутов по названию вида животного
   *
   * @param name название вида животного
   * @return список атрибут
   */
  public List<AttributeDto> getByAnimalTypeName(String name) {
    if (StringUtils.isBlank(name)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно получить атрибут по названию: передано пустое название");
    }
    return AttributeMapper.INSTANCE.toDtoList(animalTypeAttributeValueRepository.findAllByAnimalTypeName((name))).stream()
      .sorted(Comparator.comparing(AttributeDto::getPriority))
      .collect(Collectors.toList());
  }


  /**
   * Сохранение атрибута
   *
   * @param dto объект с данными атрибута {@link AttributeDto}
   */
  public void save(AttributeDto dto) {
    if (Objects.isNull(dto)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно сохранить атрибут: dto равен null");
    }
    repository.findById(dto.getName().toLowerCase()).flatMap(attribute -> attribute.getValues().stream()
      .filter(value -> !dto.getValues().contains(value.getValue()))
      .findAny()).ifPresent(value -> {
      if (animalTypeAttributeValueRepository.existsByAttributeNameAndAttributeValue(value.getValue(), value.getAttributeName())) {
        throw new LogicException(CommonErrorCode.VALIDATION_ERROR,
          String.format("Невозможно сохранить атрибут: значение '%s' атрибута '%s' используется у вида животного",
            value.getValue(), value.getAttributeName()));
      }
    });
    repository.save(AttributeMapper.INSTANCE.toEntity(dto));
  }

  @Transactional
  public void updatePriorities(List<AttributeDto> dtos) {
    if (Objects.isNull(dtos) || dtos.isEmpty()) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно обновить приоритеты атрибутов: список атрибутов пуст или null");
    }
    List<String> names = dtos.stream()
      .map(AttributeDto::getName)
      .collect(Collectors.toList());
    List<AttributeEntity> entities = repository.findAllById(names);
    Map<String, AttributeDto> dtoMap = dtos.stream()
      .collect(Collectors.toMap(AttributeDto::getName, dto -> dto));
    repository.saveAll(entities.stream()
      .peek(entity -> {
        AttributeDto dto = dtoMap.get(entity.getName());
        if (Objects.nonNull(dto)) {
          entity.setPriority(dto.getPriority());
        }
      })
      .toList()
    );
  }


  /**
   * Удаление атрибута по названию
   *
   * @param name название атрибута
   */
  public void delete(String name) {
    if (StringUtils.isBlank(name)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно удалить атрибут: передано пустое название");
    }
    repository.deleteById(name.toLowerCase());
  }

  /**
   * Получение атрибута по названию
   *
   * @param name название атрибута
   * @return сущность атрибута
   */
  private AttributeEntity findByName(String name) {
    return repository.findById(name.toLowerCase())
      .orElseThrow(() -> new LogicException(CommonErrorCode.COMMON_OBJECT_NOT_EXISTS,
        "Невозможно получить атрибут по названию: не найден атрибут с названием: " + name));
  }

  public List<AttributeEntity> findAllByNames(Set<String> names) {
    if (Objects.isNull(names) || names.isEmpty()) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Невозможно получить атрибуты по названиям: передан пустой список");
    }
    return repository.findAllByNameIn(names);
  }

  public List<AttributeValueEntity> findAllValuesByIds(Set<AttributeValuePK> ids) {
    if (Objects.isNull(ids) || ids.isEmpty()) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Невозможно получить значения атрибутов по id: передан пустой список");
    }
    return valueRepository.findAllByIdIn(ids);
  }
}
