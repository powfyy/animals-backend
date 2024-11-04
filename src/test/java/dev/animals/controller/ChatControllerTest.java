package dev.animals.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.animals.dto.MessageDto;
import dev.animals.entity.*;
import dev.animals.entity.animal.AnimalEntity;
import dev.animals.enums.GenderType;
import dev.animals.enums.PetStatus;
import dev.animals.enums.Role;
import dev.animals.mappers.MessageMapper;
import dev.animals.repositories.*;
import dev.animals.services.chat.ChatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testUser", roles = ("USER"))
@ContextConfiguration(initializers = {dev.animals.controller.ChatControllerTest.Initializer.class})
@Testcontainers
public class ChatControllerTest {
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("pethaven")
            .withUsername("postgres")
            .withPassword("sadmin");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.liquibase.enabled=true"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    ChatService chatService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthRepository authRepository;
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AnimalRepository animalRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    MessageMapper messageMapper;
    @Mock
    Principal principal;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

  UserEntity user;
  OrganizationEntity organization;

  ChatEntity chat;
  AnimalEntity pet;

    @BeforeEach
    void setData() {
      AuthEntity authOrg = new AuthEntity("testOrg", Role.ORG, passwordEncoder.encode("1234"), true);
      organization = new OrganizationEntity(null,
                "NameOrg",
                "City",
                "0000",
                "312132",
                "71234567890");
        organization.setAuth(authOrg);
      AuthEntity authUser = new AuthEntity("testUser", Role.USER, passwordEncoder.encode("1234"), true);
      user = new UserEntity("Name", "Last name", "71234567890");
        user.setAuth(authUser);
      pet = new AnimalEntity("testName", GenderType.M, PetType.DOG, LocalDate.now(), "testBreed", null, PetStatus.ACTIVE);
        authRepository.deleteAll();
        userRepository.deleteAll();
        organizationRepository.deleteAll();
        userRepository.save(user);
        organizationRepository.save(organization);

        messageRepository.deleteAll();
        chatRepository.deleteAll();
      chat = chatService.create(organization.getAuth().getUsername(), user.getAuth().getUsername());
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @BeforeEach
    void setDataRepeat() {

    }

    @Test
    @Transactional
    public void testAddMessage() throws Exception {
      MessageEntity message = new MessageEntity(
                1L,
                "Test message",
                LocalDateTime.of(2023, Month.NOVEMBER, 19, 15, 0, 0),
                chat.getId());
        message.setUserId(user.getId());
      MessageDto messageDTO = messageMapper.toDto(message);
        messageDTO.setDate("19.11.2023 15:00:00");

        when(principal.getName()).thenReturn("testUser");

        mockMvc.perform(post("/api/chats/messages").principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assertions.assertEquals(messageRepository.findAll().get(0), message);
    }

    @Test
    @Transactional
    public void testGetMessages() throws Exception {
      MessageEntity message = new MessageEntity(
                1L,
                "Test message",
                LocalDateTime.of(2023, Month.NOVEMBER, 19, 15, 0, 0),
                chat.getId());
        message.setUserId(user.getId());
        messageRepository.save(message);


        when(principal.getName()).thenReturn("testUser");


        mockMvc.perform(get("/api/chats/messages/1/?page=0&size=1").principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content[0].message").value("Test message"));
    }

    @Test
    public void testGetChats() throws Exception {
        when(principal.getName()).thenReturn("testUser");

        mockMvc.perform(get("/api/chats").principal(principal))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @Transactional
    public void testCreateChatAndAddRequestMessage() throws Exception {
      ChatEntity chat = new ChatEntity();
        chat.setId(1L);
        chat.setOrganization(organization);
        chat.setUser(user);
        pet.setOrganization(organization);
      animalRepository.save(pet);

        mockMvc.perform(post("/api/chats?orgUsername=testOrg&userUsername=testUser&petId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Assertions.assertEquals(objectMapper.writeValueAsString(chatService.findByUsernames("testOrg", "testUser")),
                objectMapper.writeValueAsString(chat));

        Assertions.assertEquals(messageRepository.count(), 1L);
    }
}