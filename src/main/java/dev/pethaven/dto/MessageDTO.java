package dev.pethaven.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class MessageDTO {
    @NotNull(message = "message can't be null")
    @NotBlank(message = "message can't be empty")
    private String message;
    @NotNull(message = "date can't be null")
    @NotBlank(message = "date can't be empty")
    private String date;
    @NotNull(message = "chatId can't be null")
    private Long chatId;
    private String organizationUsername;
    private String userUsername;
}
