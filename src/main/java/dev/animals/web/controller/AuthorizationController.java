package dev.animals.web.controller;

import dev.animals.service.OrganizationService;
import dev.animals.service.UserService;
import dev.animals.service.auth.AuthDetailsImpl;
import dev.animals.utils.JwtUtils;
import dev.animals.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthorizationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final OrganizationService organizationService;
    private final JwtUtils jwtUtils;

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
          .map(GrantedAuthority::getAuthority)
                .findFirst()
          .orElse("USER");

        return new JwtResponse(jwt, authDetails.getUsername(), role);
    }

    @Transactional
    @PostMapping("/signup/user")
    public UserDto registerUser(@RequestBody @Valid SignupUserRequest signupUserRequest) {
        return userService.create(signupUserRequest);
    }

    @Transactional
    @PostMapping("/signup/organization")
    public OrganizationDto registerOrganization(@RequestBody @Valid SignupOrganizationRequest signupOrganizationRequest) {
        return organizationService.create(signupOrganizationRequest);
    }

}
