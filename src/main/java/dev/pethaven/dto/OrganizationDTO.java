package dev.pethaven.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class OrganizationDTO {
    @Size(min = 4, message = "username must be minimum 4 chars")
    private String username;
    @Size(min = 2, message = "name organization must be minimum 2 chars")
    private String nameOrganization;
    @Size(min = 2, message = "city must be minimum 2 chars")
    private String city;
    @Size(min = 4, max = 4, message = "passport series must be 4 chars")
    private String passportSeries;
    @Size(min = 6, max = 6, message = "passport number must be 4 chars")
    private String passportNumber;
    @Size(min = 10, message = "phone number must be minimum 10 chars")
    private String phoneNumber;
}
