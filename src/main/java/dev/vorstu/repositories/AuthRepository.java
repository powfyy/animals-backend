package dev.vorstu.repositories;

import dev.vorstu.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository <Auth, Long> {
    Optional<Auth> findByUsername(String username);
    Boolean existsByUsername (String username);
}
