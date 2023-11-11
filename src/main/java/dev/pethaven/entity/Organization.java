package dev.pethaven.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name="organizations")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String nameOrganization;
    @Column (nullable = false)
    private String city;
    @Column(nullable = false)
    private String passportSeries;
    @Column(nullable = false)
    private String passportNumber;
    @Column(nullable = false)
    private String phoneNumber;

    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(nullable = false)
    private Auth auth;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<Pet> pets;

    public Organization(Long id, String nameOrganization,String city, String passportSeries, String passportNumber, String phoneNumber, Auth auth) {
        this.id = id;
        this.nameOrganization = nameOrganization;
        this.city=city;
        this.passportSeries = passportSeries;
        this.passportNumber = passportNumber;
        this.phoneNumber = phoneNumber;
        this.auth = auth;
    }
}
