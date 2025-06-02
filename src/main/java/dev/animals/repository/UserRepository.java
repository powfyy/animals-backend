package dev.animals.repository;

import dev.animals.entity.UserEntity;
import dev.animals.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByAuthUsername(String username);

  List<UserEntity> findAllByAuthUsernameIn(List<String> usernames);

  boolean existsByAuthRole(Role role);

  void deleteByAuthUsername(String username);

}
