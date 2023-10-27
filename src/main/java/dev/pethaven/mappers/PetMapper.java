package dev.pethaven.mappers;

import dev.pethaven.dto.PetDTO;
import dev.pethaven.entity.Organization;
import dev.pethaven.entity.Pet;
import dev.pethaven.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface PetMapper {
    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "user.id", target = "userId")
    PetDTO toDTO(Pet pet);
    Pet toEntity(PetDTO petDTO);
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "userSet", ignore = true)
    void updatePet(PetDTO petDTO, @MappingTarget Pet pet);

}
