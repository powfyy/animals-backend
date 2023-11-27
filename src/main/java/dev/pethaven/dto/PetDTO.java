package dev.pethaven.dto;

import lombok.Data;

import java.util.List;

@Data
public class PetDTO {
    private Long id;
    private String name;
    private String gender;
    private String typePet;
    private String birthDay;
    private String breed;
    private String description;
    private String status;
    private List<String> photoRefs;
    private String city;
    private String nameOrganization;
    private String usernameOrganization;
}
