package dev.pethaven.mappers;

import dev.pethaven.dto.ChatDTO;
import dev.pethaven.entity.Auth;
import dev.pethaven.entity.Chat;
import dev.pethaven.entity.Organization;
import dev.pethaven.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-16T17:56:14+0300",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.1.1.jar, environment: Java 1.8.0_372 (Amazon.com Inc.)"
)
public class ChatMapperImpl implements ChatMapper {

    @Override
    public ChatDTO toDTO(Chat chat) {
        if ( chat == null ) {
            return null;
        }

        ChatDTO chatDTO = new ChatDTO();

        chatDTO.setUserName( chatUserName( chat ) );
        chatDTO.setUserUsername( chatUserAuthUsername( chat ) );
        chatDTO.setOrganizationName( chatOrganizationNameOrganization( chat ) );
        chatDTO.setOrganizationUsername( chatOrganizationAuthUsername( chat ) );
        chatDTO.setId( chat.getId() );

        return chatDTO;
    }

    @Override
    public List<ChatDTO> toDtoList(List<Chat> chats) {
        if ( chats == null ) {
            return null;
        }

        List<ChatDTO> list = new ArrayList<ChatDTO>( chats.size() );
        for ( Chat chat : chats ) {
            list.add( toDTO( chat ) );
        }

        return list;
    }

    private String chatUserName(Chat chat) {
        if ( chat == null ) {
            return null;
        }
        User user = chat.getUser();
        if ( user == null ) {
            return null;
        }
        String name = user.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String chatUserAuthUsername(Chat chat) {
        if ( chat == null ) {
            return null;
        }
        User user = chat.getUser();
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

    private String chatOrganizationNameOrganization(Chat chat) {
        if ( chat == null ) {
            return null;
        }
        Organization organization = chat.getOrganization();
        if ( organization == null ) {
            return null;
        }
        String nameOrganization = organization.getNameOrganization();
        if ( nameOrganization == null ) {
            return null;
        }
        return nameOrganization;
    }

    private String chatOrganizationAuthUsername(Chat chat) {
        if ( chat == null ) {
            return null;
        }
        Organization organization = chat.getOrganization();
        if ( organization == null ) {
            return null;
        }
        Auth auth = organization.getAuth();
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
