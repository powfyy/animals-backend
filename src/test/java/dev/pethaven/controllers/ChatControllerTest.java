package dev.pethaven.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.pethaven.dto.MessageDTO;
import dev.pethaven.entity.*;
import dev.pethaven.enums.PetGender;
import dev.pethaven.enums.PetStatus;
import dev.pethaven.enums.PetType;
import dev.pethaven.enums.Role;
import dev.pethaven.mappers.MessageMapper;
import dev.pethaven.repositories.*;
import dev.pethaven.services.ChatService;
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
@ContextConfiguration(initializers = {dev.pethaven.controllers.ChatControllerTest.Initializer.class})
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
    UserRepository userRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    MessageMapper messageMapper;
    Organization organization;
    User user;
    @Mock
    Principal principal;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        authRepository.deleteAll();
        userRepository.deleteAll();
        organizationRepository.deleteAll();
        Auth authUser = new Auth("testUser", Role.USER, passwordEncoder.encode("1234"), true);
        user = new User("Name", "Last name", "71234567890");
        user.setAuth(authUser);
        userRepository.save(user);

        Auth authOrg = new Auth("testOrg", Role.ORG, passwordEncoder.encode("1234"), true);
        organization = new Organization(null,
                "NameOrg",
                "City",
                "0000",
                "312132",
                "71234567890");
        organization.setAuth(authOrg);
        organizationRepository.save(organization);
    }

    @Test
    @Transactional
    public void testAddMessage() throws Exception {
        Chat chat = chatService.createChat(organization.getAuth().getUsername(), user.getAuth().getUsername());
        Message message = new Message(
                1L,
                "Test message",
                LocalDateTime.of(2023, Month.NOVEMBER, 19, 15, 0, 0));
        message.setChat(chat);
        message.setUser(user);
        MessageDTO messageDTO = messageMapper.toDto(message);
        messageDTO.setDate("19.11.2023 15:00:00");
        when(principal.getName()).thenReturn("testUser");

        mockMvc.perform(post("/api/chats/messages").principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assertions.assertEquals(messageRepository.findAll().get(0), message);
    }

    @Test
    public void testGetMessages() throws Exception {
        Chat chat = chatService.createChat(organization.getAuth().getUsername(), user.getAuth().getUsername());
        Message message = new Message(
                1L,
                "Test message",
                LocalDateTime.of(2023, Month.NOVEMBER, 19, 15, 0, 0));
        message.setChat(chat);
        message.setUser(user);
        messageRepository.save(message);

        when(principal.getName()).thenReturn("testUser");


        mockMvc.perform(get("/api/chats/messages/1/?page=0&size=1").principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content[0].message").value("Test message"));
    }

    @Test
    public void testGetChats() throws Exception {
        chatService.createChat(organization.getAuth().getUsername(), user.getAuth().getUsername());

        when(principal.getName()).thenReturn("testUser");

        mockMvc.perform(get("/api/chats").principal(principal))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @Transactional
    public void testCreateChatAndAddRequestMessage() throws Exception {
        Chat chat = new Chat();
        chat.setId(1L);
        chat.setOrganization(organization);
        chat.setUser(user);
        Pet pet = new Pet("testName", PetGender.M, PetType.DOG, LocalDate.now(), "testBreed", null, PetStatus.ACTIVE);
        pet.setOrganization(organization);
        petRepository.save(pet);

        mockMvc.perform(post("/api/chats?orgUsername=testOrg&userUsername=testUser&petId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Assertions.assertEquals(objectMapper.writeValueAsString(chatService.findByUsernames("testOrg", "testUser")),
                objectMapper.writeValueAsString(chat));

        Assertions.assertEquals(messageRepository.count(), 1L);
    }
}