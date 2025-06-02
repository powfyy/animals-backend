package dev.animals.mapper.animal;

import dev.animals.entity.OrganizationEntity;
import dev.animals.entity.UserEntity;
import dev.animals.entity.animal.AnimalAttributeValueEntity;
import dev.animals.entity.animal.AnimalEntity;
import dev.animals.entity.animal.AnimalPhotosEntity;
import dev.animals.entity.animal.AnimalTypeEntity;
import dev.animals.entity.pk.animal.AnimalAttributeValuePK;
import dev.animals.mapper.UserMapper;
import dev.animals.web.dto.animal.AnimalDto;
import dev.animals.web.dto.animal.AnimalSaveDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(uses = {UserMapper.class})
public interface AnimalMapper {

  AnimalMapper MAPPER = Mappers.getMapper(AnimalMapper.class);

  @Mapping(target = "userOwner", source = "user")
  @Mapping(source = "animalPhotos", target = "photoRefs")
  @Mapping(source = "organization.city", target = "city")
  @Mapping(source = "organization.name", target = "organizationName")
  @Mapping(source = "organization.auth.username", target = "organizationUsername")
  @Mapping(source = "type.name", target = "type")
  @Mapping(target = "attributes", ignore = true)
  AnimalDto toDto(AnimalEntity animal);

  @AfterMapping
  default void toDtoAfter(@MappingTarget AnimalDto target, AnimalEntity source) {
    target.setAttributes(source.getAttributeValues().stream()
      .map(AnimalAttributeValueEntity::getId)
      .collect(Collectors.toMap(
        AnimalAttributeValuePK::getAttributeName,
        AnimalAttributeValuePK::getAttributeValue
      ))
    );
  }

  List<AnimalDto> toDtoList(List<AnimalEntity> animal);

  @Mapping(target = "status", expression = "java(dev.animals.enums.AnimalStatus.FREEZE)")
  @Mapping(target = "organization", source = "organization")
  @Mapping(target = "type", source = "type")
  @Mapping(target = "name", source = "source.name")
  @Mapping(target = "gender", source = "source.gender")
  @Mapping(target = "birthDay", source = "source.birthDay")
  @Mapping(target = "breed", source = "source.breed")
  @Mapping(target = "description", source = "source.description")
  @Mapping(target = "attributeValues", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "animalPhotos", ignore = true)
  @Mapping(target = "adoptionRequestUsers", ignore = true)
  AnimalEntity toEntity(AnimalSaveDto source, OrganizationEntity organization, AnimalTypeEntity type);

  @Mapping(target = "attributeValues", ignore = true)
  @Mapping(target = "organization", ignore = true)
  @Mapping(target = "user", source = "user")
  @Mapping(target = "adoptionRequestUsers", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "type", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "animalPhotos", ignore = true)
  @Mapping(target = "name", source = "source.name")
  @Mapping(target = "gender", source = "source.gender")
  @Mapping(target = "birthDay", source = "source.birthDay")
  @Mapping(target = "breed", source = "source.breed")
  @Mapping(target = "description", source = "source.description")
  void update(@MappingTarget AnimalEntity target, AnimalSaveDto source, UserEntity user);

  @AfterMapping
  default void updateAfter(@MappingTarget AnimalEntity target, AnimalSaveDto source) {
    if (Objects.nonNull(target.getId())) {
      if (Objects.nonNull(source.getStatus())) {
        target.setStatus(source.getStatus());
      }
      target.getAttributeValues().clear();
      target.getAttributeValues().addAll(source.getAttributes().entrySet().stream()
        .map(entry -> new AnimalAttributeValueEntity(
          new AnimalAttributeValuePK(source.getId(), target.getType().getName(), entry.getKey().toLowerCase(), entry.getValue().toLowerCase())
        ))
        .toList());
    }
  }

  default String mapAnimalPhotosToString(AnimalPhotosEntity animalPhotos) {
    return animalPhotos.getPhotoRef();
  }
}
