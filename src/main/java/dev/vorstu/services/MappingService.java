package dev.vorstu.services;


import dev.vorstu.dto.OrganizationDTO;
import dev.vorstu.dto.UserDTO;
import dev.vorstu.entity.Organization;
import dev.vorstu.entity.User;
import org.springframework.stereotype.Service;

@Service
public class MappingService {
    public static UserDTO mapToUserDTO(User entity){
        UserDTO dto = new UserDTO();
        dto.setName(entity.getName());
        dto.setLastname(entity.getLastname());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setUsername(entity.getAuth().getUsername());
        return dto;
    }
    public User mapToUSER(UserDTO dto){
        User entity = new User();
        entity.setName(dto.getName());
        entity.setLastname(dto.getLastname());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.getAuth().setUsername(dto.getUsername());
        return entity;
    }
    public static OrganizationDTO mapToOrganizationDTO(Organization entity){
        OrganizationDTO dto = new OrganizationDTO();
        dto.setNameOrganization(entity.getNameOrganization());
        dto.setPassportSeries(entity.getPassportSeries());
        dto.setPassportNumber(entity.getPassportNumber());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setUsername(entity.getAuth().getUsername());
        return dto;
    }
}
