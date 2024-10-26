package dev.animals.repositories;

import dev.animals.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByAuthUsername(String username);

  void deleteByAuthUsername(String username);
}
