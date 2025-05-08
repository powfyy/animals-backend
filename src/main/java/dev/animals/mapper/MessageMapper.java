package dev.animals.mapper;

import dev.animals.entity.MessageEntity;
import dev.animals.web.dto.MessageDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public interface MessageMapper {

  MessageMapper MAPPER = Mappers.getMapper(MessageMapper.class);

  @Mapping(target = "senderName", ignore = true)
  @Mapping(source = "chat.id", target = "chatId")
  @Mapping(source = "organization.auth.username", target = "organizationUsername")
  @Mapping(source = "user.auth.username", target = "userUsername")
  MessageDto toDto(MessageEntity message);

  @AfterMapping
  default void toDtoAfter(@MappingTarget MessageDto target, MessageEntity source) {
    target.setSenderName(
      Objects.nonNull(source.getUser()) ? source.getUser().getName() : source.getOrganization().getName()
    );
  }
}
