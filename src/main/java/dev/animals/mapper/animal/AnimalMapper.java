package dev.animals.mapper.animal;

import dev.animals.entity.animal.AnimalEntity;
import dev.animals.entity.animal.AnimalPhotosEntity;
import dev.animals.web.dto.animal.AnimalDto;
import dev.animals.web.dto.animal.AnimalSaveDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

@Mapper
public interface AnimalMapper {

  AnimalMapper MAPPER = Mappers.getMapper(AnimalMapper.class);

  @Mapping(source = "animalPhotos", target = "photoRefs")
  @Mapping(source = "organization.city", target = "city")
  @Mapping(source = "organization.name", target = "organizationName")
  @Mapping(source = "organization.auth.username", target = "organizationUsername")
  @Mapping(source = "type.name", target = "type")
  AnimalDto toDto(AnimalEntity animal);

  List<AnimalDto> toDtoList(List<AnimalEntity> animal);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", expression = "java(dev.animals.enums.AnimalStatus.ACTIVE)")
  @Mapping(target = "organization", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "animalPhotos", ignore = true)
  @Mapping(target = "userSet", ignore = true)
  @Mapping(target = "attributeValues", ignore = true)
  @Mapping(target = "type", ignore = true)
  AnimalEntity toEntity(AnimalSaveDto source);

  @Mapping(target = "attributeValues", ignore = true)
  @Mapping(target = "organization", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "userSet", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "type", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "animalPhotos", ignore = true)
  void update(AnimalSaveDto source, @MappingTarget AnimalEntity target);

  @AfterMapping
  default void updateAfter(@MappingTarget AnimalEntity target, AnimalSaveDto source) {
    if (Objects.nonNull(source.getStatus())) {
      target.setStatus(source.getStatus());
    }
  }

  default String mapPetPhotosToString(AnimalPhotosEntity petPhotos) {
    return petPhotos.getPhotoRef();
  }
}
