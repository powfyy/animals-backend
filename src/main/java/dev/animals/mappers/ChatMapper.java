package dev.animals.mappers;

import dev.animals.dto.ChatDto;
import dev.animals.entity.ChatEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ChatMapper {

  ChatMapper MAPPER = Mappers.getMapper(ChatMapper.class);

  @Mapping(source = "user.name", target = "userName")
  @Mapping(source = "user.auth.username", target = "userUsername")
  @Mapping(source = "organization.nameOrganization", target = "organizationName")
  @Mapping(source = "organization.auth.username", target = "organizationUsername")
  ChatDto toDto(ChatEntity chat);

  @Mapping(source = "user.name", target = "userName")
  @Mapping(source = "user.auth.username", target = "userUserName")
  @Mapping(source = "organization.nameOrganization", target = "organizationName")
  @Mapping(source = "user.name", target = "organizationUserName")
  List<ChatDto> toDtoList(List<ChatEntity> chats);
}
