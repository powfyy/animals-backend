package dev.animals.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChatDto {

    @NotNull(message = "id can't be null")
    private Long id;

  @NotNull(message = "organizationUsername can't be null")
    @NotBlank(message = "organizationUsername can't be empty")
    private String organizationUsername;

  private String organizationName;

  @NotNull(message = "userUsername can't be null")
    @NotBlank(message = "userUsername can't be empty")
    private String userUsername;

  private String userName;

}
