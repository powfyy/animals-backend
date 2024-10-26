package dev.animals.services;

import dev.animals.entity.AuthEntity;
import dev.animals.repositories.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuthDetailsServiceImpl implements UserDetailsService {

    private final AuthRepository authRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthEntity auth = authRepository
                .findByUsername(username)
          .orElseThrow(() -> new UsernameNotFoundException("UserEntity Not Found with username: " + username));
        return AuthDetailsImpl.build(auth);
    }

}