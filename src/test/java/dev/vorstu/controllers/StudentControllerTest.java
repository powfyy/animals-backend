package dev.vorstu.controllers;

import dev.vorstu.dto.StudentDTO;
import dev.vorstu.entity.Student;
import dev.vorstu.entity.User;
import dev.vorstu.repositories.StudentRepository;
import dev.vorstu.repositories.UserRepository;
import dev.vorstu.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class StudentControllerTest {

    @InjectMocks
    private StudentController studentController;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private Principal principal;

    @Test
    public void testGetStudent() {
        User user = new User();
        user.setUsername("test_user");
        Student student = new Student();
        student.setId(1L);
        user.setStudent(student);

        when(userRepository.findByUsername(principal.getName())).thenReturn(Optional.of(user));
        when(userService.findById(student.getId())).thenReturn(new StudentDTO(student));

        StudentDTO[] result = studentController.getStudent(principal);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(student.getId(), result[0].getId());
    }
    @Test
    public void testUpdateStudent() {
        User user = new User();
        user.setUsername("test_user");
        Student student = new Student();
        student.setId(1L);
        user.setStudent(student);
        Student updatedStudent = new Student();
        updatedStudent.setFio("Updated Name");
        updatedStudent.setGroup("Updated Group");
        updatedStudent.setPhoneNumber(7777);

        when(userRepository.findByUsername(principal.getName())).thenReturn(Optional.of(user));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        Student result = studentController.updateStudent(principal, updatedStudent);

        assertNotNull(result);
        assertEquals(updatedStudent.getFio(), result.getFio());
        assertEquals(updatedStudent.getGroup(), result.getGroup());
        assertEquals(updatedStudent.getPhoneNumber(), result.getPhoneNumber());
    }
}
