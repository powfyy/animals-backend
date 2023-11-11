package dev.pethaven.mappers;

import dev.pethaven.dto.UserDTO;
import dev.pethaven.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Set;

@Mapper
public interface UserMapper {
    @Mapping(source = "auth.username", target = "username")
    UserDTO toDTO (User user);

    @Mapping(source = "auth.username", target = "username")
    Set<UserDTO> toDtoSet (Set <User> userSet);

    void updateUser(UserDTO userDTO, @MappingTarget User user);
}
