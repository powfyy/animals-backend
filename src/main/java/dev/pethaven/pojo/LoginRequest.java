package dev.pethaven.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class LoginRequest {
    private String username;
    private String password;
}
