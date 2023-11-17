package dev.pethaven.repositories;

import dev.pethaven.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findAllByChatId(Long chatId);
}
