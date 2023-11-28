package dev.pethaven.services;

import dev.pethaven.dto.SignupUserRequest;
import dev.pethaven.dto.UserDTO;
import dev.pethaven.entity.Auth;
import dev.pethaven.entity.User;
import dev.pethaven.enums.Role;
import dev.pethaven.exception.AlreadyExistsException;
import dev.pethaven.exception.NotFoundException;
import dev.pethaven.mappers.UserMapper;
import dev.pethaven.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Service
@Validated
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthService authService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserMapper userMapper;

    public UserDTO getCurrentUser(Principal principal) {
        return userMapper.toDTO(findByUsername(principal.getName()));
    }

    public UserDTO createUser(@Valid SignupUserRequest signupUserRequest) {
        if (authService.existsByUsername(signupUserRequest.getUsername())) {
            throw new AlreadyExistsException("Username already exists");
        }
        Auth newAuth = new Auth(
                signupUserRequest.getUsername(),
                Role.USER,
                passwordEncoder.encode(signupUserRequest.getPassword()),
                true
        );
        User newUser = new User(
                signupUserRequest.getName(),
                signupUserRequest.getLastname(),
                signupUserRequest.getPhoneNumber()
        );
        newUser.setAuth(newAuth);
        save(newUser);
        return userMapper.toDTO(newUser);
    }

    public UserDTO updateUser(@Valid UserDTO updatedUser) {
        User user = findByUsername(updatedUser.getUsername());
        userMapper.updateUser(updatedUser, user);
        save(user);
        return userMapper.toDTO(user);
    }

    public void deleteCurrentUser(Principal principal) {
        userRepository.deleteByUsername(principal.getName());
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User is not found"));
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
