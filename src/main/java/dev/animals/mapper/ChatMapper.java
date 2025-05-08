package dev.animals.mapper;

import dev.animals.entity.ChatEntity;
import dev.animals.entity.MessageEntity;
import dev.animals.web.dto.ChatDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;

@Mapper
public interface ChatMapper {

  ChatMapper MAPPER = Mappers.getMapper(ChatMapper.class);

  @Mapping(target = "lastMessageDate", ignore = true)
  @Mapping(target = "lastMessage", ignore = true)
  @Mapping(source = "user.name", target = "userName")
  @Mapping(source = "user.auth.username", target = "userUsername")
  @Mapping(source = "organization.name", target = "organizationName")
  @Mapping(source = "organization.auth.username", target = "organizationUsername")
  ChatDto toDto(ChatEntity chat);

  @AfterMapping
  default void toDtoAfter(@MappingTarget ChatDto target, ChatEntity source) {
    if (!CollectionUtils.isEmpty(source.getMessages())) {
      MessageEntity lastMessage = source.getMessages().stream()
        .max(Comparator.comparing(MessageEntity::getDate))
        .orElse(null);
      target.setLastMessage(lastMessage.getMessage());
      target.setLastMessageDate(lastMessage.getDate());
    }
  }

  List<ChatDto> toDtoList(List<ChatEntity> chats);
}
