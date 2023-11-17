package dev.pethaven.services;

import dev.pethaven.dto.UserDTO;
import dev.pethaven.entity.Auth;
import dev.pethaven.entity.User;
import dev.pethaven.exception.NotFoundException;
import dev.pethaven.mappers.UserMapper;
import dev.pethaven.repositories.AuthRepository;
import dev.pethaven.repositories.PetRepository;
import dev.pethaven.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;

@Service
@Validated
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthRepository authRepository;
    @Autowired
    PetRepository petRepository;

    @Autowired
    UserMapper userMapper;

    public UserDTO getCurrentUser(Principal principal) {
        Auth currentAuth = (authRepository.findByUsername(principal.getName()))
                .orElseThrow(() -> new NotFoundException("Auth not found"));
        return userMapper.toDTO(userRepository
                .findByAuthId(currentAuth.getId())
                .orElseThrow(() -> new NotFoundException("User not found")));
    }

    public void updateUser(@Valid  UserDTO updatedUser) {
        User user = userRepository
                .findByAuthId(authRepository
                        .findByUsername(updatedUser.getUsername())
                        .orElseThrow(() -> new NotFoundException("Auth not found"))
                        .getId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        userMapper.updateUser(updatedUser, user);
        userRepository.save(user);
    }

    public void deleteCurrentUser(Principal principal) {
        userRepository.deleteByAuthId(authRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new NotFoundException("Auth not found"))
                .getId());
    }

    public void requestForPet(Principal principal, @NotNull(message = "Id cannot be null") Long petId) {
        Auth currentAuth = (authRepository.findByUsername(principal.getName()))
                                          .orElseThrow(() -> new NotFoundException("Auth not found"));
        User currentUser = userRepository.findByAuthId(currentAuth.getId())
                                         .orElseThrow(() -> new NotFoundException("User not found"));
        currentUser.getPetSet().add(petRepository.findById(petId)
                                                 .orElseThrow(() -> new NotFoundException("Pet not found")));
        userRepository.save(currentUser);
    }

    public Map<String, Boolean> checkRequest(Principal principal, @NotNull(message = "Id cannot be null") Long petId) {
        User user = userRepository
                .findByAuthId(authRepository
                        .findByUsername(principal.getName())
                        .orElseThrow(() -> new NotFoundException("Auth not found"))
                        .getId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (user.getPetSet().contains(petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundException("Pet not found")))) {
            return Collections.singletonMap("isThereRequest", true);
        }
        return Collections.singletonMap("isThereRequest", false);
    }
}
