package dev.pethaven.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class UserDTO {
    @Size(min = 4, message = "username must be minimum 4 chars")
    private String username;
    @Size(min = 2, message = "name must be minimum 2 chars")
    private String name;
    @NotNull(message = "lastname is mandatory")
    private String lastname;
    @Size(min = 10, message = "phone number must be minimum 10 chars")
    private String phoneNumber;
}
