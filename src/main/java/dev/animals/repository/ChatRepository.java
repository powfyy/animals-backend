package dev.animals.repository;

import dev.animals.entity.ChatEntity;
import dev.animals.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

  @Query("SELECT c FROM ChatEntity c JOIN c.user u JOIN c.organization o WHERE u.auth.username = :userUsername AND o.auth.username = :orgUsername")
  Optional<ChatEntity> findChatByUsernames(String orgUsername, String userUsername);

  @EntityGraph(attributePaths = "messages")
  @Query("""
    SELECT c FROM ChatEntity c
    JOIN c.user u
    JOIN c.organization o
    WHERE u.auth.username = :username OR o.auth.username = :username
    """)
  Page<ChatEntity> findAllByUsername(String username, Pageable pageable);

  @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM ChatEntity c JOIN c.user u JOIN c.organization o " +
    "WHERE (c.id = :id AND u.auth.username = :username) OR (c.id = :id AND o.auth.username = :username)")
  boolean isMember(Long id, String username);

  @Query("""
    SELECT c FROM ChatEntity c
    JOIN c.user u
    JOIN c.organization o
    WHERE c.id = :id and (u.auth.username = :username OR o.auth.username = :username)
    """)
  Optional<ChatEntity> findByIdAndUsername(Long id, String username);

  String user(UserEntity user);
}
