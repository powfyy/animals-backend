package dev.pethaven.mappers;

import dev.pethaven.dto.PetDTO;
import dev.pethaven.entity.Organization;
import dev.pethaven.entity.Pet;
import dev.pethaven.entity.PetPhotos;
import dev.pethaven.entity.User;
import dev.pethaven.pojo.SavePet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface PetMapper {
    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "petPhotos", target = "photoRefs")
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
    @Mapping(target = "petPhotos", ignore = true)
    void updatePet(PetDTO petDTO, @MappingTarget Pet pet);

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
