package dev.animals.mapper;

import dev.animals.entity.AuthEntity;
import dev.animals.entity.OrganizationEntity;
import dev.animals.web.dto.organization.OrganizationCityNameDto;
import dev.animals.web.dto.organization.OrganizationDto;
import dev.animals.web.dto.organization.OrganizationShortDto;
import dev.animals.web.dto.organization.SignupOrganizationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrganizationMapper {

  OrganizationMapper MAPPER = Mappers.getMapper(OrganizationMapper.class);

  @Mapping(target = "messages", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "chats", ignore = true)
  @Mapping(target = "animals", ignore = true)
  @Mapping(target = "auth", expression = "java(this.toAuth(source))")
  OrganizationEntity toEntity(SignupOrganizationRequest source);

  @Mapping(target = "role", expression = "java(dev.animals.enums.Role.ORG)")
  @Mapping(target = "enable", expression = "java(true)")
  AuthEntity toAuth(SignupOrganizationRequest source);

  @Mapping(source = "auth.username", target = "username")
  OrganizationDto toDto(OrganizationEntity organization);

  @Mapping(target = "username", source = "auth.username")
  OrganizationShortDto toShortDto(OrganizationEntity organization);

  List<OrganizationShortDto> toShortDtoList(List<OrganizationEntity> organizations);

  OrganizationCityNameDto toDtoCityName(OrganizationEntity organization);

  @Mapping(target = "animals", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auth", ignore = true)
  @Mapping(target = "chats", ignore = true)
  @Mapping(target = "messages", ignore = true)
  void updateOrganization(OrganizationDto organizationDto, @MappingTarget OrganizationEntity org);
}
