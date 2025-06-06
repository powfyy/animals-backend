package dev.animals.mapper.animal;

import dev.animals.entity.animal.AnimalTypeAttributeValueEntity;
import dev.animals.entity.animal.AnimalTypeEntity;
import dev.animals.entity.attribute.AttributeValueEntity;
import dev.animals.entity.pk.AnimalTypeAttributeValuePK;
import dev.animals.web.dto.animal.AnimalTypeDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.Objects;
import java.util.stream.Collectors;

@Mapper
public interface AnimalTypeMapper {

  AnimalTypeMapper INSTANCE = Mappers.getMapper(AnimalTypeMapper.class);

  @Mapping(target = "attributes", ignore = true)
  AnimalTypeDto toDto(AnimalTypeEntity source);

  @AfterMapping
  default void toDtoAfter(@MappingTarget AnimalTypeDto target, AnimalTypeEntity source) {
    if (Objects.nonNull(source.getAttributes())) {
      target.setAttributes(source.getAttributes().stream()
        .map(AnimalTypeAttributeValueEntity::getAttribute)
        .collect(Collectors.groupingBy(
          AttributeValueEntity::getAttributeName,
          Collectors.mapping(AttributeValueEntity::getValue, Collectors.toSet())
        ))
      );
    }
  }

  default Page<AnimalTypeDto> toDtoPage(Page<AnimalTypeEntity> source) {
    return source.map(this::toDto);
  }

  @Mapping(target = "attributes", ignore = true)
  AnimalTypeEntity toEntity(AnimalTypeDto source);

  @Mapping(target = "attributes", ignore = true)
  AnimalTypeEntity update(AnimalTypeDto source, @MappingTarget AnimalTypeEntity target);

  @AfterMapping
  default void updateAfter(@MappingTarget AnimalTypeEntity target, AnimalTypeDto source) {
    target.getAttributes().addAll(source.getAttributes().entrySet().stream()
      .flatMap(entry -> entry.getValue().stream()
        .map(value -> new AnimalTypeAttributeValueEntity(new AnimalTypeAttributeValuePK(source.getName(), entry.getKey(), value))))
      .toList());
  }
}
