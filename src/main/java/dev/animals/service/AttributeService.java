package dev.animals.service;

import dev.animals.entity.attribute.AttributeEntity;
import dev.animals.exception.LogicException;
import dev.animals.exception.helper.CommonErrorCode;
import dev.animals.mapper.AttributeMapper;
import dev.animals.repository.animal.AttributeRepository;
import dev.animals.web.dto.AttributeDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AttributeService {

  private final AttributeRepository repository;

  /**
   * Получение атрибутов
   *
   * @param page номер страницы
   * @param size размер страницы
   * @return список атрибутов
   */
  public Page<AttributeDto> getAll(int page, int size) {
    return AttributeMapper.INSTANCE.toDtoPage(repository.findAll(PageRequest.of(page, size)));
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
   * Сохранение атрибута
   *
   * @param dto объект с данными атрибута {@link AttributeDto}
   */
  public void save(AttributeDto dto) {
    if (Objects.isNull(dto)) {
      throw new LogicException(CommonErrorCode.VALIDATION_ERROR, "Невозможно сохранить атрибут: dto равен null");
    }
    repository.save(AttributeMapper.INSTANCE.toEntity(dto));
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
}
