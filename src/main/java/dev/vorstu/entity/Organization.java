package dev.vorstu.entity;

import lombok.*;

import javax.persistence.*;

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
    @Column(nullable = false)
    private String passportSeries;
    @Column(nullable = false)
    private String passportNumber;
    @Column(nullable = false)
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    private Auth auth;
}
