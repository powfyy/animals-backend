package dev.vorstu.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupUserRequest {
    private String username;
    private String password;
    private String name;
    private String lastname;
    private String phoneNumber;
}
