package dev.vorstu;

import dev.vorstu.entity.*;
import dev.vorstu.repositories.AuthRepository;
import dev.vorstu.repositories.OrganizationRepository;
import dev.vorstu.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Initializer {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    public void initial() {
//        Auth authUser = new Auth(null, "user", Role.USER, passwordEncoder.encode("1234"),true);
//        User user = new User(null, "Иван", "Лоренц", "79513334343", authUser);
//        authRepository.save(authUser);
//        userRepository.save(user);
//        Auth authOrg = new Auth(null, "teddy", Role.ORG, passwordEncoder.encode( "ffff"),true);
//        Organization organization = new Organization(null, "TeddyFood", "2020", "123456", "79203335544",authOrg);
//        authRepository.save(authOrg);
//        organizationRepository.save(organization);
    }
}
