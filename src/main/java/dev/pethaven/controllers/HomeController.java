package dev.pethaven.controllers;

import dev.pethaven.dto.OrganizationDTO;
import dev.pethaven.dto.OrganizationDtoCityName;
import dev.pethaven.dto.PetDTO;
import dev.pethaven.entity.*;
import dev.pethaven.exception.NotFoundException;
import dev.pethaven.mappers.OrganizationMapper;
import dev.pethaven.mappers.PetMapper;
import dev.pethaven.pojo.FilterFields;
import dev.pethaven.pojo.MessageResponse;
import dev.pethaven.repositories.AuthRepository;
import dev.pethaven.repositories.OrganizationRepository;
import dev.pethaven.repositories.PetRepository;
import dev.pethaven.repositories.UserRepository;
import dev.pethaven.services.OrganizationService;
import dev.pethaven.services.PetService;
import dev.pethaven.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    PetRepository petRepository;
    @Autowired
    PetMapper petMapper;
    @Autowired
    PetService petService;
    @Autowired
    UserService userService;

    @Autowired
    OrganizationService organizationService;

    @PostMapping(value = "/pets/{id}")
    public ResponseEntity<?> requestForPet(@PathVariable("id") Long petId, Principal principal) {
        userService.requestForPet(principal,petId);
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
        return organizationService.getOrganizationCityAndName();
    }

    @GetMapping(value = "/pets/{id}")
    public PetDTO getPet(@PathVariable("id") @NotNull(message = "Id cannot be null") Long petId) {
        return petMapper.toDTO(petRepository.findById(petId)
                .orElseThrow(()-> new NotFoundException("Pet is not found")));
    }

    @GetMapping(value = "/pets/{id}/request")
    public Map<String, Boolean> checkRequest(@PathVariable("id") Long petId, Principal principal) {
        return userService.checkRequest(principal, petId);
    }
}
