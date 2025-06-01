package dev.animals.mapper;

import dev.animals.entity.animal.AnimalTypeAttributeValueEntity;
import dev.animals.entity.attribute.AttributeEntity;
import dev.animals.entity.attribute.AttributeValueEntity;
import dev.animals.entity.pk.animal.AttributeValuePK;
import dev.animals.web.dto.AttributeDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.*;
import java.util.stream.Collectors;

@Mapper
public interface AttributeMapper {

  AttributeMapper INSTANCE = Mappers.getMapper(AttributeMapper.class);

  AttributeDto toDto(AttributeEntity attributeEntity);

  default List<AttributeDto> toDtoList(List<AnimalTypeAttributeValueEntity> source) {
    if (Objects.isNull(source)) {
      return null;
    }
    Map<String, AttributeDto> attributeMap = new HashMap<>();
    source.forEach(atav ->
      attributeMap.computeIfAbsent(
          atav.getId().getAttributeName(),
          name -> new AttributeDto(name, atav.getAttribute().getAttribute().getPriority(), new HashSet<>()))
        .getValues().add(atav.getId().getAttributeValue())
    );
   return new ArrayList<>(attributeMap.values());

  }

  default Page<AttributeDto> toDtoPage(Page<AttributeEntity> attributes) {
    return Objects.nonNull(attributes) ? attributes.map(this::toDto) : null;
  }

  @Mapping(target = "values", ignore = true)
  @Mapping(target = "name", expression = "java(dto.getName().toLowerCase())")
  AttributeEntity toEntity(AttributeDto dto);

  @AfterMapping
  default void toEntity(@MappingTarget AttributeEntity target, AttributeDto source) {
    if (Objects.nonNull(source.getValues())) {
      String name = source.getName().toLowerCase();
      target.setValues(source.getValues().stream()
        .map(value -> {
            String lowerCaseValue = value.toLowerCase();
            return new AttributeValueEntity(
              new AttributeValuePK(lowerCaseValue, name), name, lowerCaseValue, target);
          }
        )
        .collect(Collectors.toList()));
    }
  }

  default String toValueString(AttributeValueEntity attributeValueEntity) {
    return Objects.nonNull(attributeValueEntity) ? attributeValueEntity.getValue() : null;
  }
}
