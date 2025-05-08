package dev.animals.mapper;

import dev.animals.entity.ChatEntity;
import dev.animals.web.dto.ChatDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChatMapper {

  ChatMapper MAPPER = Mappers.getMapper(ChatMapper.class);

  @Mapping(target = "lastMessage", source = "lastMessageText")
  @Mapping(source = "user.name", target = "userName")
  @Mapping(source = "user.auth.username", target = "userUsername")
  @Mapping(source = "organization.name", target = "organizationName")
  @Mapping(source = "organization.auth.username", target = "organizationUsername")
  ChatDto toDto(ChatEntity chat);

}
