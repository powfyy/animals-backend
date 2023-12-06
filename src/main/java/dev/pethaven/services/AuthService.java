package dev.pethaven.services;

import dev.pethaven.entity.Auth;
import dev.pethaven.enums.Role;
import dev.pethaven.exception.NotFoundException;
import dev.pethaven.repositories.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AuthService {
    @Autowired
    AuthRepository authRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Auth findByUsername(String username) {
        return authRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Auth is not found"));
    }

    public Auth createAuth(String username, Role role, String password) {
        return authRepository.save(new Auth(
                username,
                role,
                passwordEncoder.encode(password),
                true
        ));
    }

    public boolean existsByUsername(String username) {
        return authRepository.existsByUsername(username);
    }
}
