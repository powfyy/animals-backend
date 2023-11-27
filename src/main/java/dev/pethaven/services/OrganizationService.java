package dev.pethaven.services;

import dev.pethaven.dto.OrganizationDTO;
import dev.pethaven.dto.OrganizationDtoCityName;
import dev.pethaven.dto.SignupOrganizationRequest;
import dev.pethaven.entity.Auth;
import dev.pethaven.entity.Organization;
import dev.pethaven.enums.Role;
import dev.pethaven.exception.AlreadyExistsException;
import dev.pethaven.exception.NotFoundException;
import dev.pethaven.mappers.OrganizationMapper;
import dev.pethaven.repositories.AuthRepository;
import dev.pethaven.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class OrganizationService {
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    AuthService authService;
    @Autowired
    OrganizationMapper organizationMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    public OrganizationDTO getCurrentOrganization(Principal principal) {
        return organizationMapper.toDTO(findByUsername(principal.getName()));
    }

    public List<OrganizationDtoCityName> getOrganizationCityAndName() {
        return organizationRepository.findAll().stream()
                .map(el -> organizationMapper.toDtoCityName(el))
                .collect(Collectors.toList());
    }

    public OrganizationDTO createOrganization(SignupOrganizationRequest signupOrganizationRequest) {
        if (authService.existsByUsername(signupOrganizationRequest.getUsername())) {
            throw new AlreadyExistsException("Username already exists");
        }
        Auth newAuth = new Auth(
                signupOrganizationRequest.getUsername(),
                Role.ORG,
                passwordEncoder.encode(signupOrganizationRequest.getPassword()),
                true
        );
        Organization newOrganization = new Organization(
                signupOrganizationRequest.getNameOrganization(),
                signupOrganizationRequest.getCity(),
                signupOrganizationRequest.getPassportNumber(),
                signupOrganizationRequest.getPassportSeries(),
                signupOrganizationRequest.getPhoneNumber(),
                newAuth
        );
        organizationRepository.save(newOrganization);
        return organizationMapper.toDTO(newOrganization);
    }
    public OrganizationDTO updateOrganization(@Valid OrganizationDTO updatedOrganization) {
        Organization organization = findByUsername(updatedOrganization.getUsername());
        organizationMapper.updateOrganization(updatedOrganization, organization);
        organizationRepository.save(organization);
        return organizationMapper.toDTO(organization);
    }

    public void deleteCurrentOrganization(Principal principal) {
        organizationRepository.deleteByUsername(principal.getName());
    }

    public Organization findByUsername(String username){
        return organizationRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Organization is not found"));
    }
}
