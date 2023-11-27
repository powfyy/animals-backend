package dev.pethaven.repositories;

import dev.pethaven.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u JOIN u.auth a WHERE a.username = :username")
    public Optional<User> findByUsername(String username);

    public Set<User> findAllByPetSetId(Long petId);

    @Query("DELETE FROM User u WHERE u.id IN (SELECT u2.id FROM User u2 JOIN u2.auth a WHERE a.username = :username)")
    public void deleteByUsername(String username);
}
