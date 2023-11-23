package dev.pethaven.mappers;

import dev.pethaven.dto.PetDTO;
import dev.pethaven.entity.Pet;
import dev.pethaven.entity.PetPhotos;
import dev.pethaven.dto.SavePet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface PetMapper {

    @Mapping(source = "petPhotos", target = "photoRefs")
    @Mapping(source = "organization.city", target = "city")
    @Mapping(source = "organization.nameOrganization", target = "nameOrganization")
    @Mapping(source = "organization.auth.username", target = "usernameOrganization")
    PetDTO toDTO(Pet pet);
    @Mapping(target = "organization",ignore = true)
    @Mapping(target = "user",ignore = true)
    @Mapping(target = "petPhotos",ignore = true)
    @Mapping(target = "userSet",ignore = true)
    Pet toEntity(PetDTO petDTO);
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "status",ignore = true)
    @Mapping(target = "organization",ignore = true)
    @Mapping(target = "user",ignore = true)
    @Mapping(target = "petPhotos",ignore = true)
    @Mapping(target = "userSet",ignore = true)
    Pet toEntity (SavePet newPetInfo);

    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "userSet", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "petPhotos", ignore = true)
    void updatePet(SavePet petDTO, @MappingTarget Pet pet);

    default String mapPetPhotosToString(PetPhotos petPhotos) {
        return petPhotos.getPhotoRef();
    }
}
