package dev.pethaven.mappers;

import dev.pethaven.dto.ChatDTO;
import dev.pethaven.entity.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ChatMapper {
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "user.auth.username", target = "userUsername")
    @Mapping(source = "organization.nameOrganization", target = "organizationName")
    @Mapping(source = "organization.auth.username", target = "organizationUsername")
    ChatDTO toDTO (Chat chat);
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "user.auth.username", target = "userUserName")
    @Mapping(source = "organization.nameOrganization", target = "organizationName")
    @Mapping(source = "user.name", target = "organizationUserName")
    List<ChatDTO> toDtoList (List<Chat> chats);
}
