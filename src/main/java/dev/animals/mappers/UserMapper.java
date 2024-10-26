package dev.animals.mappers;

import dev.animals.dto.UserDto;
import dev.animals.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface UserMapper {

  UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "auth.username", target = "username")
    UserDto toDto(UserEntity user);

    @Mapping(source = "auth.username", target = "username")
    Set<UserDto> toDtoSet(Set<UserEntity> userSet);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "auth", ignore = true)
    @Mapping(target = "animalSet", ignore = true)
    @Mapping(target = "chats", ignore = true)
    @Mapping(target = "messages", ignore = true)
    void updateUser(UserDto userDTO, @MappingTarget UserEntity user);
}
