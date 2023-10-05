package dev.vorstu.controllers;

import dev.vorstu.auth.JwtUtils;
import dev.vorstu.entity.Auth;
import dev.vorstu.entity.Organization;
import dev.vorstu.entity.Role;
import dev.vorstu.entity.User;
import dev.vorstu.pojo.*;
import dev.vorstu.repositories.AuthRepository;
import dev.vorstu.repositories.OrganizationRepository;
import dev.vorstu.repositories.UserRepository;
import dev.vorstu.services.AuthDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@Slf4j
public class AuthorizationController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AuthRepository authRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt =jwtUtils.generateJwtToken(authentication);

        AuthDetailsImpl authDetails = (AuthDetailsImpl) authentication.getPrincipal();
        String role = authDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .findFirst()
                .orElseGet(() -> "USER");;

        return ResponseEntity.ok(new JwtResponse(jwt,
                authDetails.getId(),
                authDetails.getUsername(), role));
    }

    @PostMapping("/signup/user")
    public ResponseEntity<?> registerUser(@RequestBody SignupUserRequest signupUserRequest) {

        if (authRepository.existsByUsername(signupUserRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is exist"));
        }
        if (signupUserRequest.getUsername() == null || signupUserRequest.getUsername() == ""){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username cannot be empty"));
        }
        Auth newAuth = new Auth (null, signupUserRequest.getUsername(), Role.USER,passwordEncoder.encode(signupUserRequest.getPassword()),true );
        User newUser = new User(null, signupUserRequest.getName(),signupUserRequest.getLastname(), signupUserRequest.getPhoneNumber(), newAuth);
        authRepository.save(newAuth);
        userRepository.save(newUser);
        return ResponseEntity.ok(new MessageResponse("User CREATED"));
    }

    @PostMapping ("/signup/organization")
    public ResponseEntity <?> registerOrganization(@RequestBody SignupOrganizationRequest signupOrganizationRequest){
        if (authRepository.existsByUsername(signupOrganizationRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is exist"));
        }
        if (signupOrganizationRequest.getUsername() == null || signupOrganizationRequest.getUsername() == ""){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username cannot be empty"));
        }
        Auth newAuth = new Auth(null, signupOrganizationRequest.getUsername(), Role.ORG, passwordEncoder.encode(signupOrganizationRequest.getPassword()),true);
        Organization newOrganization = new Organization(null, signupOrganizationRequest.getNameOrganization(), signupOrganizationRequest.getPassportNumber(), signupOrganizationRequest.getPassportSeries(), signupOrganizationRequest.getPhoneNumber(), newAuth);
        authRepository.save(newAuth);
        organizationRepository.save(newOrganization);
        return ResponseEntity.ok(new MessageResponse("Organization CREATED"));
    }
}
