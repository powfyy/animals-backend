package dev.animals.mapper;

import dev.animals.entity.MessageEntity;
import dev.animals.web.dto.MessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {

  MessageMapper MAPPER = Mappers.getMapper(MessageMapper.class);

  @Mapping(source = "chat.id", target = "chatId")
  @Mapping(source = "organization.auth.username", target = "organizationUsername")
  @Mapping(source = "user.auth.username", target = "userUsername")
  MessageDto toDto(MessageEntity message);
}
