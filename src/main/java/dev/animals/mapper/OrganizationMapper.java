package dev.animals.mapper;

import dev.animals.entity.AuthEntity;
import dev.animals.entity.OrganizationEntity;
import dev.animals.web.dto.OrganizationCityNameDto;
import dev.animals.web.dto.OrganizationDto;
import dev.animals.web.dto.SignupOrganizationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

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

  OrganizationCityNameDto toDtoCityName(OrganizationEntity organization);

  @Mapping(target = "animals", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auth", ignore = true)
  @Mapping(target = "chats", ignore = true)
  @Mapping(target = "messages", ignore = true)
  void updateOrganization(OrganizationDto organizationDto, @MappingTarget OrganizationEntity org);
}
