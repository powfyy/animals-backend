package dev.pethaven.controllers;

import dev.pethaven.dto.PetDTO;
import dev.pethaven.entity.*;
import dev.pethaven.mappers.PetMapper;
import dev.pethaven.repositories.AuthRepository;
import dev.pethaven.repositories.OrganizationRepository;
import dev.pethaven.repositories.PetRepository;
import dev.pethaven.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ProfileeControllerTest {
    @InjectMocks
    private ProfileeController profileeController;
    @Mock
    private AuthRepository authRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PetRepository petRepository;
    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Principal principal;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(profileeController).build();
    }

    @Test
    void testGetAllPets() throws Exception {

        MockitoAnnotations.openMocks(this);
        Organization organization =new Organization(1L, "TeddyFood", "2020", "123456", "79203335544",
                new Auth(1L, "username", Role.ORG, passwordEncoder.encode("ffff"), true));
        Pet pet = new Pet(1L, "Буся", PetGender.F, PetType.DOG, LocalDate.of(2019, 3, 12),
                "Метис помчи", PetStatus.ACTIVE, organization);
        PetDTO petDTO = PetMapper.INSTANCE.toDTO(pet);
        List<Pet> petList = new ArrayList<>();
        petList.add(pet);
        when(principal.getName()).thenReturn("username");
        when(authRepository.findByUsername("username")).thenReturn(Optional.of(pet.getOrganization().getAuth()));
        when(petRepository.findByOrganizationId(1l)).thenReturn(petList);
        when(organizationRepository.findByAuthId(anyLong())).thenReturn(organization);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/profile/pets")
                        .with(request -> {
                            request.setUserPrincipal(principal);
                            return request;
                        }))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(petDTO.getName()));
    }
    @Test
    public void testGetUserInfo() throws Exception{
        Auth auth = new Auth();
        auth.setId(1l);
        User user = new User();
        user.setId(2l);
        user.setName("bob");

        when(principal.getName()).thenReturn("username");
        when(authRepository.findByUsername("username")).thenReturn(Optional.of(auth));
        when(userRepository.findByAuthId(1l)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/profile/user")
                        .with(request -> {
                            request.setUserPrincipal(principal);
                            return request;
                        }))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(user.getName()));
    }
    @Test
    public void testGetOrganizationInfo() throws Exception{

    }
}

//
//    @Test
//    public void testGetAllStudents() throws Exception {
//        List<StudentDTO> studentDTOList = new ArrayList<>();
//        studentDTOList.add(new StudentDTO(new Student("Gosha", "VM", 1111)));
//        studentDTOList.add(new StudentDTO(new Student("Valera", "Po", 2222)));
//
//        when(userService.findAll()).thenReturn(studentDTOList);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/students"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(studentDTOList.size()))
//                .andDo(print());
//
//        verify(userService, times(1)).findAll();
//    }
//
//    @Test
//    public void testCreateStudent() throws Exception {
//        Student student = new Student("Gelya", "IVT", 0111);
//
//        when(studentRepository.save(any(Student.class))).thenReturn(student);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/students")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(JsonUtils.asJsonString(student)))
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//
//        verify(studentRepository, times(1)).save(any(Student.class));
//    }

//    @Test
//    public void testDeleteStudent() throws Exception {
//        Student student = new Student("Gelya", "IVT", 0111);
//
//        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/students/{id}", student.getId()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//
//        verify(studentRepository, times(1)).delete(student);
//    }