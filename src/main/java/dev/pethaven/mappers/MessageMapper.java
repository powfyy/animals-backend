package dev.pethaven.mappers;

import dev.pethaven.dto.MessageDTO;
import dev.pethaven.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MessageMapper {
    @Mapping(source = "organization.auth.username", target = "organizationUsername")
    @Mapping(source = "user.auth.username", target = "userUsername")
    MessageDTO toDto (Message message);
}
