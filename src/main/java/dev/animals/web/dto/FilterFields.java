package dev.animals.web.dto;

import lombok.Data;

@Data
public class FilterFields {
    private String name;
    private String city;
    private String nameOrganization;
    private String gender;
    private String petType;

}
