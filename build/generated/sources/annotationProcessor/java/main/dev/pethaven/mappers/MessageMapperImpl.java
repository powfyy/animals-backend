package dev.pethaven.mappers;

import dev.pethaven.dto.MessageDTO;
import dev.pethaven.entity.Auth;
import dev.pethaven.entity.Chat;
import dev.pethaven.entity.Message;
import dev.pethaven.entity.Organization;
import dev.pethaven.entity.User;
import java.time.format.DateTimeFormatter;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-16T17:57:37+0300",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.1.1.jar, environment: Java 1.8.0_372 (Amazon.com Inc.)"
)
public class MessageMapperImpl implements MessageMapper {

    @Override
    public MessageDTO toDto(Message message) {
        if ( message == null ) {
            return null;
        }

        String arg0 = null;
        String arg1 = null;
        Long arg2 = null;
        String arg3 = null;
        String arg4 = null;

        MessageDTO messageDTO = new MessageDTO( arg0, arg1, arg2, arg3, arg4 );

        messageDTO.setChatId( messageChatId( message ) );
        messageDTO.setOrganizationUsername( messageOrganizationAuthUsername( message ) );
        messageDTO.setUserUsername( messageUserAuthUsername( message ) );
        messageDTO.setMessage( message.getMessage() );
        if ( message.getDate() != null ) {
            messageDTO.setDate( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( message.getDate() ) );
        }

        return messageDTO;
    }

    private Long messageChatId(Message message) {
        if ( message == null ) {
            return null;
        }
        Chat chat = message.getChat();
        if ( chat == null ) {
            return null;
        }
        Long id = chat.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String messageOrganizationAuthUsername(Message message) {
        if ( message == null ) {
            return null;
        }
        Organization organization = message.getOrganization();
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

    private String messageUserAuthUsername(Message message) {
        if ( message == null ) {
            return null;
        }
        User user = message.getUser();
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
