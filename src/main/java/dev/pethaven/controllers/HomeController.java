package dev.pethaven.controllers;

import dev.pethaven.dto.OrganizationDtoCityName;
import dev.pethaven.dto.PetDTO;
import dev.pethaven.dto.FilterFields;
import dev.pethaven.services.OrganizationService;
import dev.pethaven.services.PetService;
import dev.pethaven.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    @Autowired
    PetService petService;
    @Autowired
    OrganizationService organizationService;

    @PostMapping(value = "/pets/{id}")
    public void requestForPet(@PathVariable("id") Long petId, Principal principal) {
        petService.requestForPet(principal,petId);
    }

    @PostMapping(value = "/pets")
    public Page<PetDTO> getFilteredPets(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "15") int size,
            @RequestBody FilterFields filterFields) {
        return petService.getFilteredPets(page,size,filterFields);
    }

    @GetMapping(value = "/organizations")
    public List<OrganizationDtoCityName> getAllOrganizations() {
        return organizationService.getOrganizationCityAndName();
    }

    @GetMapping(value = "/pets/{id}")
    public PetDTO getPet(@PathVariable("id") @NotNull(message = "Id cannot be null") Long petId) {
        return petService.getPetDTOById(petId);
    }

    @GetMapping(value = "/pets/{id}/request")
    public Map<String, Boolean> checkRequest(@PathVariable("id") Long petId, Principal principal) {
        return petService.checkRequest(principal, petId);
    }
}
