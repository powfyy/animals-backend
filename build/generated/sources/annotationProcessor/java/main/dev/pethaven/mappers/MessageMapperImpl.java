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
    date = "2023-11-28T13:57:09+0300",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 20.0.1 (Oracle Corporation)"
)
public class MessageMapperImpl implements MessageMapper {

    @Override
    public MessageDTO toDto(Message message) {
        if ( message == null ) {
            return null;
        }

        Long chatId = null;
        String organizationUsername = null;
        String userUsername = null;
        String message1 = null;
        String date = null;

        chatId = messageChatId( message );
        organizationUsername = messageOrganizationAuthUsername( message );
        userUsername = messageUserAuthUsername( message );
        message1 = message.getMessage();
        if ( message.getDate() != null ) {
            date = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( message.getDate() );
        }

        MessageDTO messageDTO = new MessageDTO( message1, date, chatId, organizationUsername, userUsername );

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
