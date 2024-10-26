package dev.animals.mappers;

import dev.animals.dto.MessageDto;
import dev.animals.entity.MessageEntity;
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
