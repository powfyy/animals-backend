package dev.animals.web.controller;

import dev.animals.service.chat.ChatService;
import dev.animals.service.chat.MessageService;
import dev.animals.web.dto.ChatDto;
import dev.animals.web.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;
  private final MessageService messageService;

  @GetMapping
  public Page<ChatDto> getChats(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = "35") int size,
                                Principal principal) {
    return chatService.getAll(page, size, principal.getName());
  }

  @GetMapping(value = "/message/{id}/")
  public Page<MessageDto> getMessages(@PathVariable("id") @NotNull(message = "Id cannot be null") Long chatId,
                                      @RequestParam(required = false, defaultValue = "0") int page,
                                      @RequestParam(required = false, defaultValue = "35") int size,
                                      Principal principal) {
    return messageService.getAllByChatId(chatId, page, size, principal.getName());
  }

  @PostMapping(value = "/message")
  public void addMessage(@RequestBody @Valid MessageDto messageDto, Principal principal) {
    messageService.create(messageDto, principal.getName());
  }

  @PostMapping
  public void createChatAndAddRequestMessage(@RequestParam("orgUsername") String orgUsername,
                                             @RequestParam("userUsername") String userUsername,
                                             @RequestParam("animalId") Long animalId) {
    messageService.createRequestMessage(animalId, chatService.create(orgUsername, userUsername));
  }
}
