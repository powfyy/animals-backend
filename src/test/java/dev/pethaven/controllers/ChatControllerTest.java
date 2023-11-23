package dev.pethaven.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.pethaven.dto.MessageDTO;
import dev.pethaven.entity.*;
import dev.pethaven.repositories.*;
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

import java.security.Principal;
import java.time.LocalDate;

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
    ChatRepository chatRepository;
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
        Auth authUser = new Auth(null, "testUser", Role.USER, passwordEncoder.encode("1234"), true);
        user = new User(null, "Name", "Last name", "71234567890", authUser);
        userRepository.save(user);
        authRepository.save(authUser);

        Auth authOrg = new Auth(null, "testOrg", Role.ORG, passwordEncoder.encode("1234"), true);
        organization = new Organization(null,
                "NameOrg",
                "City",
                "0000",
                "312132",
                "71234567890",
                authOrg);
        authRepository.save(authOrg);
        organizationRepository.save(organization);

    }

    @Test
    public void testAddMessage() throws Exception {
        Chat chat = new Chat(null, user, organization);
        chatRepository.save(chat);
        MessageDTO messageDTO = new MessageDTO(
                "Test message",
                "19.11.2023 15:00:00",
                chat.getId(),
                null,
                "testUser");

        mockMvc.perform(post("/api/chats/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.message").value("Message added"));
    }

    @Test
    public void testGetMessages() throws Exception {
        Chat chat = new Chat(null, user, organization);

        mockMvc.perform(get("/api/chats/messages/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetChats() throws Exception {
        Chat chat = new Chat(null, user, organization);
        chatRepository.save(chat);

        when(principal.getName()).thenReturn("testUser");

        mockMvc.perform(get("/api/chats").principal(principal))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testCreateChatAndAddRequestMessage() throws Exception {
        Pet pet = new Pet(null, "testName", PetGender.M, PetType.DOG, LocalDate.now(), "testBreed", null, PetStatus.ACTIVE, organization);
        petRepository.save(pet);
        mockMvc.perform(post("/api/chats?orgUsername=testOrg&userUsername=testUser&petId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.message").value("RequestMessage sent"));
    }
}