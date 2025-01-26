package dev.animals.web.controller;

import dev.animals.service.UserService;
import dev.animals.web.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
@Tag(name = "UserController", description = "API для работы с пользователями")
public class UserController {

  private final UserService userService;

  @GetMapping("/user")
  public UserDto getCurrentUser(Principal principal) {
    return userService.getByUsername(principal.getName());
  }

  @PutMapping("/user")
  public UserDto updateUser(@RequestBody @Valid UserDto updatedUser) {
    return userService.update(updatedUser);
  }

  @DeleteMapping("/user")
  public void deleteUser(Principal principal) {
    userService.deleteByUsername(principal.getName());
  }

}
