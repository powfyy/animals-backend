package dev.pethaven.services;

import dev.pethaven.entity.Auth;
import dev.pethaven.exception.NotFoundException;
import dev.pethaven.repositories.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AuthService {
    @Autowired
    AuthRepository authRepository;

    public Auth findByUsername(String username) {
        return authRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Auth is not found"));
    }

    public boolean existsByUsername (String username) {
        return authRepository.existsByUsername(username);
    }
}
