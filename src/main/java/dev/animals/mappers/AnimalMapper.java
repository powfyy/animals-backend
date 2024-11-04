package dev.animals.mappers;

import dev.animals.dto.AnimalDto;
import dev.animals.dto.AnimalSaveDto;
import dev.animals.entity.animal.AnimalEntity;
import dev.animals.entity.animal.AnimalPhotosEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AnimalMapper {

  AnimalMapper MAPPER = Mappers.getMapper(AnimalMapper.class);

  @Mapping(source = "animalPhotos", target = "photoRefs")
    @Mapping(source = "organization.city", target = "city")
    @Mapping(source = "organization.nameOrganization", target = "nameOrganization")
    @Mapping(source = "organization.auth.username", target = "usernameOrganization")
  AnimalDto toDto(AnimalEntity animal);

  List<AnimalDto> toDtoList(List<AnimalEntity> animal);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "status",ignore = true)
    @Mapping(target = "organization",ignore = true)
    @Mapping(target = "user",ignore = true)
    @Mapping(target = "animalPhotos", ignore = true)
    @Mapping(target = "userSet",ignore = true)
    AnimalEntity toEntity(AnimalSaveDto newPetInfo);

    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "user",ignore = true)
    @Mapping(target = "userSet", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "animalPhotos", ignore = true)
    void updatePet(AnimalSaveDto petDTO, @MappingTarget AnimalEntity pet);

  default String mapPetPhotosToString(AnimalPhotosEntity petPhotos) {
        return petPhotos.getPhotoRef();
    }
}
