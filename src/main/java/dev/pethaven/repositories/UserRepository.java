package dev.pethaven.repositories;

import dev.pethaven.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByAuthId (Long authId);
    public Set<User> findAllByPetSetId(Long petId);
}
