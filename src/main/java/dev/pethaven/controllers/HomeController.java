package dev.pethaven.controllers;

import dev.pethaven.entity.Auth;
import dev.pethaven.entity.User;
import dev.pethaven.repositories.AuthRepository;
import dev.pethaven.repositories.OrganizationRepository;
import dev.pethaven.repositories.PetRepository;
import dev.pethaven.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    AuthRepository authRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    PetRepository petRepository;

    @PostMapping (value = "/{id}")
    public void requestForPet (@PathVariable ("id") Long petId, Principal user){
        Auth currentAuth=(authRepository.findByUsername(user.getName())).get();
        User currentUser = userRepository.findByAuthId(currentAuth.getId());
        currentUser.getPetSet().add(petRepository.findById(petId).get());
        userRepository.save(currentUser);
    }
}
