package dev.animals.repository;

import dev.animals.entity.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

  Page<MessageEntity> findAllByChatId(Long chatId, Pageable pageable);
}
