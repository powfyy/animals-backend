package dev.pethaven.repositories;

import dev.pethaven.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c JOIN c.user u JOIN c.organization o WHERE u.auth.username = :userUsername AND o.auth.username = :orgUsername")
    Optional<Chat> findChatByUsernames(String orgUsername, String userUsername);

    @Query("SELECT c FROM Chat c JOIN c.user u JOIN c.organization o " +
            "WHERE u.auth.username = :username OR o.auth.username = :username")
    Page<Chat> findChatsByUsername(String username, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Chat c JOIN c.user u JOIN c.organization o " +
            "WHERE (c.id = :id AND u.auth.username = :username) OR (c.id = :id AND o.auth.username = :username)")
    boolean isParticipant(Long id, String username);

}
