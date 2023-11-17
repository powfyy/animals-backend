package dev.pethaven.repositories;

import dev.pethaven.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByOrganizationId(Long organizationId);

    List<Chat> findByUserId(Long userId);

    boolean existsByOrganizationAuthUsernameAndUserAuthUsername(String orgUsername, String UserUsername);

    Optional<Chat> findByOrganizationAuthUsernameAndUserAuthUsername(String orgUsername, String UserUsername);
}
