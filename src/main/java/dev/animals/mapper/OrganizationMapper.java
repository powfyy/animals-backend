package dev.animals.mapper;

import dev.animals.entity.OrganizationEntity;
import dev.animals.web.dto.OrganizationCityNameDto;
import dev.animals.web.dto.OrganizationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrganizationMapper {

  OrganizationMapper MAPPER = Mappers.getMapper(OrganizationMapper.class);

  @Mapping(source = "auth.username", target = "username")
  OrganizationDto toDTO(OrganizationEntity organization);

  OrganizationCityNameDto toDtoCityName(OrganizationEntity organization);

  @Mapping(target = "animals", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auth", ignore = true)
  @Mapping(target = "chats", ignore = true)
  @Mapping(target = "messages", ignore = true)
  void updateOrganization(OrganizationDto organizationDto, @MappingTarget OrganizationEntity org);
}
