package dev.pethaven.mappers;

import dev.pethaven.dto.UserDTO;
import dev.pethaven.entity.Auth;
import dev.pethaven.entity.User;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-19T15:54:48+0300",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.1.1.jar, environment: Java 1.8.0_372 (Amazon.com Inc.)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setUsername( userAuthUsername( user ) );
        userDTO.setName( user.getName() );
        userDTO.setLastname( user.getLastname() );
        userDTO.setPhoneNumber( user.getPhoneNumber() );

        return userDTO;
    }

    @Override
    public Set<UserDTO> toDtoSet(Set<User> userSet) {
        if ( userSet == null ) {
            return null;
        }

        Set<UserDTO> set = new HashSet<UserDTO>( Math.max( (int) ( userSet.size() / .75f ) + 1, 16 ) );
        for ( User user : userSet ) {
            set.add( toDTO( user ) );
        }

        return set;
    }

    private String userAuthUsername(User user) {
        if ( user == null ) {
            return null;
        }
        Auth auth = user.getAuth();
        if ( auth == null ) {
            return null;
        }
        String username = auth.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }
}
