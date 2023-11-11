package dev.pethaven.mappers;

import dev.pethaven.dto.OrganizationDTO;
import dev.pethaven.dto.OrganizationDtoCityName;
import dev.pethaven.entity.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Mapper
public interface OrganizationMapper {
    @Mapping(source = "auth.username", target = "username")
    OrganizationDTO toDTO (Organization organization);
    OrganizationDtoCityName toDtoCityName (Organization organization);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "auth", ignore = true)
    @Mapping(target = "pets", ignore = true)
    void updateOrganization (OrganizationDTO organizationDTO, @MappingTarget Organization org);
}
