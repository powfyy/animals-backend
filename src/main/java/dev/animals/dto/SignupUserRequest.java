package dev.animals.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class SignupUserRequest {
    @Size(min = 4, max = 20, message = "username must be minimum 4 chars")
    private String username;
    @Size(min = 4, max = 20, message = "password must be minimum 4 chars")
    private String password;
    @Size(min = 2, max = 20, message = "name must be minimum 2 chars")
    private String name;
    @NotNull(message = "lastname is mandatory")
    private String lastname;
    @Size(min = 10, max = 20, message = "phone number must be minimum 10 chars")
    private String phoneNumber;
}
