package dev.pethaven.controllers;

import dev.pethaven.auth.JwtUtils;
import dev.pethaven.dto.*;
import dev.pethaven.entity.Auth;
import dev.pethaven.entity.Organization;
import dev.pethaven.enums.Role;
import dev.pethaven.entity.User;
import dev.pethaven.repositories.AuthRepository;
import dev.pethaven.repositories.OrganizationRepository;
import dev.pethaven.repositories.UserRepository;
import dev.pethaven.services.AuthDetailsImpl;
import dev.pethaven.services.OrganizationService;
import dev.pethaven.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/api")
@Slf4j
public class AuthorizationController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;
    @Autowired
    OrganizationService organizationService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public JwtResponse authUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        AuthDetailsImpl authDetails = (AuthDetailsImpl) authentication.getPrincipal();
        String role = authDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .findFirst()
                .orElseGet(() -> "USER");

        return new JwtResponse(jwt, authDetails.getId(), authDetails.getUsername(), role);
    }

    @Transactional
    @PostMapping("/signup/user")
    public UserDTO registerUser(@Valid @RequestBody SignupUserRequest signupUserRequest) {
        return userService.createUser(signupUserRequest);
    }

    @Transactional
    @PostMapping("/signup/organization")
    public OrganizationDTO registerOrganization(@Valid @RequestBody SignupOrganizationRequest signupOrganizationRequest) {
        return organizationService.createOrganization(signupOrganizationRequest);
    }

}
