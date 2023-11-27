package dev.pethaven.mappers;

import dev.pethaven.dto.OrganizationDTO;
import dev.pethaven.dto.OrganizationDtoCityName;
import dev.pethaven.entity.Auth;
import dev.pethaven.entity.Organization;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-27T14:01:52+0300",
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
        organizationDTO.setCity( organization.getCity() );
        organizationDTO.setPassportSeries( organization.getPassportSeries() );
        organizationDTO.setPassportNumber( organization.getPassportNumber() );
        organizationDTO.setPhoneNumber( organization.getPhoneNumber() );

        return organizationDTO;
    }

    @Override
    public OrganizationDtoCityName toDtoCityName(Organization organization) {
        if ( organization == null ) {
            return null;
        }

        OrganizationDtoCityName organizationDtoCityName = new OrganizationDtoCityName();

        organizationDtoCityName.setNameOrganization( organization.getNameOrganization() );
        organizationDtoCityName.setCity( organization.getCity() );

        return organizationDtoCityName;
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
