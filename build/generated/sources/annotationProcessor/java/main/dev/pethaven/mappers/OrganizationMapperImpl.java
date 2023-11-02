package dev.pethaven.mappers;

import dev.pethaven.dto.OrganizationDTO;
import dev.pethaven.entity.Auth;
import dev.pethaven.entity.Organization;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-02T12:52:32+0300",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.1.1.jar, environment: Java 1.8.0_372 (Amazon.com Inc.)"
)
public class OrganizationMapperImpl implements OrganizationMapper {

    @Override
    public OrganizationDTO toDTO(Organization organization) {
        if ( organization == null ) {
            return null;
        }

        OrganizationDTO organizationDTO = new OrganizationDTO();

        organizationDTO.setUsername( organizationAuthUsername( organization ) );
        organizationDTO.setNameOrganization( organization.getNameOrganization() );
        organizationDTO.setPassportSeries( organization.getPassportSeries() );
        organizationDTO.setPassportNumber( organization.getPassportNumber() );
        organizationDTO.setPhoneNumber( organization.getPhoneNumber() );
        organizationDTO.setCity( organization.getCity() );

        return organizationDTO;
    }

    @Override
    public void updateOrganization(OrganizationDTO organizationDTO, Organization org) {
        if ( organizationDTO == null ) {
            return;
        }

        org.setNameOrganization( organizationDTO.getNameOrganization() );
        org.setCity( organizationDTO.getCity() );
        org.setPassportSeries( organizationDTO.getPassportSeries() );
        org.setPassportNumber( organizationDTO.getPassportNumber() );
        org.setPhoneNumber( organizationDTO.getPhoneNumber() );
    }

    private String organizationAuthUsername(Organization organization) {
        if ( organization == null ) {
            return null;
        }
        Auth auth = organization.getAuth();
        if ( auth == null ) {
            return null;
        }
        String username = auth.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }
}
