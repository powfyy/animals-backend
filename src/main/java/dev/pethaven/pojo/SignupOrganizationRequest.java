package dev.pethaven.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupOrganizationRequest {
    private String username;
    private String password;
    private String nameOrganization;
    private String passportSeries;
    private String passportNumber;
    private String phoneNumber;
}
