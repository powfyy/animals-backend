package dev.vorstu.controllers;


import dev.vorstu.dto.OrganizationDTO;
import dev.vorstu.dto.UserDTO;
import dev.vorstu.entity.Auth;
import dev.vorstu.repositories.AuthRepository;
import dev.vorstu.repositories.OrganizationRepository;
import dev.vorstu.repositories.UserRepository;
import dev.vorstu.services.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping ("profil")
public class ProfilController {
    @Autowired
    AuthRepository authRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    @GetMapping("user")
    public UserDTO getUserInfo (Principal user) {
        Auth currentAuth=(authRepository.findByUsername(user.getName())).get();
        return MappingService.mapToUserDTO(userRepository.findByAuthId(currentAuth.getId()));
    }
    @GetMapping ("organization")
    public OrganizationDTO getOrganizationInfo (Principal user){
        Auth currentAuth=(authRepository.findByUsername(user.getName())).get();
        return MappingService.mapToOrganizationDTO(organizationRepository.findByAuthId(currentAuth.getId()));
    }
}
