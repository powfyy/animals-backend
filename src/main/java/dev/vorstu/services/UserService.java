package dev.vorstu.services;


import dev.vorstu.dto.UserDTO;
import dev.vorstu.entity.User;
import dev.vorstu.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

//    public List<StudentDTO> findAll() {
//        return StreamSupport.stream(studentRepository.findAll().spliterator(), false)
//                .map(MappingService::mapToStudentDTO)
//                .collect(Collectors.toList());
//    }
//
    public UserDTO findById(Long id) {
        return MappingService.mapToUserDTO(userRepository.findById(id)
                        .orElse(new User())
        );
    }
}
