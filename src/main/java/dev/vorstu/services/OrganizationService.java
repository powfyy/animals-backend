package dev.vorstu.services;

import dev.vorstu.dto.OrganizationDTO;
import dev.vorstu.entity.Organization;
import dev.vorstu.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {
    @Autowired
    OrganizationRepository organizationRepository;
    public OrganizationDTO findById(Long id) {
        return MappingService.mapToOrganizationDTO(organizationRepository.findById(id)
                .orElse(new Organization())
        );
    }}
