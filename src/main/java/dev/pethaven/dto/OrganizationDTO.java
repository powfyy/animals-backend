package dev.pethaven.dto;

import lombok.Data;

@Data
public class OrganizationDTO {
    String nameOrganization;
    String passportSeries;
    String passportNumber;
    String phoneNumber;
    String username;
    String city;
}
