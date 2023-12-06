package dev.pethaven;

import dev.pethaven.dto.MessageDTO;
import dev.pethaven.entity.Chat;
import dev.pethaven.entity.Message;
import dev.pethaven.mappers.MessageMapper;
import dev.pethaven.repositories.*;
import dev.pethaven.services.ChatService;
import dev.pethaven.services.MessageService;
import dev.pethaven.services.UserService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

@Component
public class Initializer {
//    @Autowired
//    ChatService chatService;
//    @Autowired
//    MessageRepository messageRepository;
//    @Autowired
//    MessageService messageService;
//    @Autowired
//    MessageMapper messageMapper;

    public void initial() {

    }

//    @Transactional
//    public void func() {
//        {
//            Chat chat = chatService.findById(144L);
//            Message message = new Message(
//                    "Test message",
//                    LocalDateTime.of(2023, Month.NOVEMBER, 19, 15, 0, 0),
//                    chat.getId());
//            message.setUser(chat.getUser());
//            messageRepository.save(message);
//            System.out.println(message.getId());
//
//            Message message1 = messageRepository.findById(message.getId()).get();
//            System.out.println(message1.getChat().getId());
//        }
//    }

}
