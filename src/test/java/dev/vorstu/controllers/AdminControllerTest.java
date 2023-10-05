package dev.vorstu.controllers;

import dev.vorstu.dto.StudentDTO;
import dev.vorstu.entity.Student;
import dev.vorstu.repositories.StudentRepository;
import dev.vorstu.repositories.UserRepository;
import dev.vorstu.services.UserService;
import dev.vorstu.utils.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AdminControllerTest {
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private Principal principal;
    @InjectMocks
    private AdminController adminController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void testGetAllStudents() throws Exception {
        List<StudentDTO> studentDTOList = new ArrayList<>();
        studentDTOList.add(new StudentDTO(new Student("Gosha", "VM", 1111)));
        studentDTOList.add(new StudentDTO(new Student("Valera", "Po", 2222)));

        when(userService.findAll()).thenReturn(studentDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/students"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(studentDTOList.size()))
                .andDo(print());

        verify(userService, times(1)).findAll();
    }

    @Test
    public void testCreateStudent() throws Exception {
        Student student = new Student("Gelya", "IVT", 0111);

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(student)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(studentRepository, times(1)).save(any(Student.class));
    }

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
}