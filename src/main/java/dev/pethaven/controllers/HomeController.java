package dev.pethaven.controllers;

import dev.pethaven.dto.OrganizationDTO;
import dev.pethaven.dto.OrganizationDtoCityName;
import dev.pethaven.dto.PetDTO;
import dev.pethaven.entity.*;
import dev.pethaven.mappers.OrganizationMapper;
import dev.pethaven.mappers.PetMapper;
import dev.pethaven.pojo.FilterFields;
import dev.pethaven.pojo.MessageResponse;
import dev.pethaven.repositories.AuthRepository;
import dev.pethaven.repositories.OrganizationRepository;
import dev.pethaven.repositories.PetRepository;
import dev.pethaven.repositories.UserRepository;
import dev.pethaven.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    PetMapper petMapper;
    @Autowired
    OrganizationMapper organizationMapper;
    @Autowired
    PetService petService;

    @PostMapping(value = "/pets/{id}")
    public ResponseEntity<?> requestForPet(@PathVariable("id") Long petId, Principal user) {
        Auth currentAuth = (authRepository.findByUsername(user.getName())).get();
        User currentUser = userRepository.findByAuthId(currentAuth.getId());
        currentUser.getPetSet().add(petRepository.findById(petId).get());
        userRepository.save(currentUser);
        return ResponseEntity.ok().body(new MessageResponse("Request sent"));
    }

    @PostMapping(value = "/pets")
    public Page<PetDTO> getFilteredPets(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "12") int size,
            @RequestBody FilterFields filterFields) {
        return petService.getFilteredPets(page,size,filterFields);
    }

    @GetMapping(value = "/organizations")
    public List<OrganizationDtoCityName> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(el -> organizationMapper.toDtoCityName(el))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/pets/{id}")
    public PetDTO getPet(@PathVariable("id") Long petId) {
        return petMapper.toDTO(petRepository.findById(petId).get());
    }

    @GetMapping(value = "/pets/{id}/request")
    public Map<String, Boolean> checkRequest(@PathVariable("id") Long petId, Principal principal) {
        User user = userRepository.findByAuthId(authRepository.findByUsername(principal.getName()).get().getId());
        if (user.getPetSet().contains(petRepository.findById(petId).get())) {
            return Collections.singletonMap("isThereRequest", true);
        }
        return Collections.singletonMap("isThereRequest", false);
    }
}
