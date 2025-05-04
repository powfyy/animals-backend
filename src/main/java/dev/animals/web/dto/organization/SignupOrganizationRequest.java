package dev.animals.web.dto.organization;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter @Setter
public class SignupOrganizationRequest {

  @Size(min = 4, max = 20, message = "username must be minimum 4 chars")
  private String username;
  @Size(min = 4, max = 20, message = "password must be minimum 4 chars")
  private String password;
  @Size(min = 2, max = 20, message = "name organization must be minimum 2 chars")
  private String name;
  @Size(min = 2, max = 20, message = "city must be minimum 2 chars")
  private String city;
  @Size(min = 4, max = 4, message = "passport series must be 4 chars")
  private String passportSeries;
  @Size(min = 6, max = 6, message = "passport number must be 4 chars")
  private String passportNumber;
  @Size(min = 10, max = 20, message = "phone number must be minimum 10 chars")
  private String phoneNumber;
}
