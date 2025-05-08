package dev.animals.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

  @NotNull(message = "message can't be null")
  @NotBlank(message = "message can't be empty")
  private String message;

  @NotNull(message = "chatId can't be null")
  private Long chatId;
  private String organizationUsername;
  private String userUsername;
  private String senderName;
  private LocalDateTime date;
}
