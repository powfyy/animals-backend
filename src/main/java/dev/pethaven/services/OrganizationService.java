package dev.pethaven.services;

import dev.pethaven.dto.OrganizationDTO;
import dev.pethaven.dto.OrganizationDtoCityName;
import dev.pethaven.entity.Auth;
import dev.pethaven.entity.Organization;
import dev.pethaven.exception.NotFoundException;
import dev.pethaven.mappers.OrganizationMapper;
import dev.pethaven.repositories.AuthRepository;
import dev.pethaven.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    AuthRepository authRepository;
    @Autowired
    OrganizationMapper organizationMapper;

    public OrganizationDTO getCurrentOrganization(Principal principal) {
        Auth currentAuth = (authRepository.findByUsername(principal.getName())).orElseThrow(() -> new NotFoundException("Auth not found"));
        return organizationMapper.toDTO(organizationRepository.findByAuthId(currentAuth.getId())
                .orElseThrow(() -> new NotFoundException("Organization not found")));
    }

    public List<OrganizationDtoCityName> getOrganizationCityAndName() {
        return organizationRepository.findAll().stream()
                .map(el -> organizationMapper.toDtoCityName(el))
                .collect(Collectors.toList());
    }

    public void updateOrganization(@Valid OrganizationDTO updatedOrganization) {
        Auth currentAuth = (authRepository.findByUsername(updatedOrganization.getUsername())).orElseThrow(() -> new NotFoundException("Auth not found"));
        Organization organization = organizationRepository.findByAuthId(currentAuth.getId())
                .orElseThrow(() -> new NotFoundException("Organization not found"));
        organizationMapper.updateOrganization(updatedOrganization, organization);
        organizationRepository.save(organization);
    }

    public void deleteCurrentOrganization(Principal principal) {
        organizationRepository.deleteByAuthId(authRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new NotFoundException("Auth not found"))
                .getId());
    }
}
