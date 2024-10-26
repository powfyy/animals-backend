package dev.animals.mappers;

import dev.animals.dto.OrganizationDto;
import dev.animals.dto.OrganizationDtoCityName;
import dev.animals.entity.OrganizationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrganizationMapper {

  OrganizationMapper MAPPER = Mappers.getMapper(OrganizationMapper.class);

  @Mapping(source = "auth.username", target = "username")
  OrganizationDto toDTO(OrganizationEntity organization);

  OrganizationDtoCityName toDtoCityName(OrganizationEntity organization);

  @Mapping(target = "animals", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auth", ignore = true)
  @Mapping(target = "chats", ignore = true)
  @Mapping(target = "messages", ignore = true)
  void updateOrganization(OrganizationDto organizationDto, @MappingTarget OrganizationEntity org);
}
