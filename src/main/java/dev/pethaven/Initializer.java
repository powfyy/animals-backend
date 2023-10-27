package dev.pethaven;

import dev.pethaven.entity.*;
import dev.pethaven.repositories.AuthRepository;
import dev.pethaven.repositories.OrganizationRepository;
import dev.pethaven.repositories.PetRepository;
import dev.pethaven.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Initializer {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    public void initial() {
//        Auth authUser = new Auth(null, "legaviy", Role.USER, passwordEncoder.encode("7777"),true);
//        User user = new User(null, "Вован", "Хлеб", "79513332431", authUser);
//        authRepository.save(authUser);
//        userRepository.save(user);
//        Auth authOrg = new Auth(null, "teddy", Role.ORG, passwordEncoder.encode( "ffff"),true);
//        Organization organization = new Organization(null, "TeddyFood", "2020", "123456", "79203335544",authOrg);
//        authRepository.save(authOrg);
//        organizationRepository.save(organization);
//        petRepository.save(new Pet(null,"Буся", PetGender.F, PetType.DOG, LocalDate.of(2019,3,12), "помчи", PetStatus.ACTIVE,organizationRepository.findById(73l).orElse(null)));
//        petRepository.save(new Pet (null, "Клёпа", PetGender.F, PetType.CAT, LocalDate.of(2023, 2,15), null, PetStatus.ACTIVE, organizationRepository.findById(73l).orElse(null)));
//        petRepository.save(new Pet (null,"Мася", PetGender.F, PetType.CAT, LocalDate.of(2018,6,19), "британской", PetStatus.ACTIVE, organizationRepository.findById(73l).orElse(null)));
//        petRepository.save(new Pet (null, "Киса", PetGender.F, PetType.CAT, LocalDate.of(2020,10,8),"сиамской", PetStatus.ACTIVE, organizationRepository.findById(73l).orElse(null)));
//        petRepository.save(new Pet (null,"Леша", PetGender.M, PetType.DOG, LocalDate.of(2019,1,16),"шпица", PetStatus.ACTIVE, organizationRepository.findById(73l).orElse(null)) );
//        petRepository.save(new Pet (null,"Шарик", PetGender.M, PetType.DOG, LocalDate.of(2023, 7, 4), null, PetStatus.ACTIVE, organizationRepository.findById(73l).orElse(null)));
//        petRepository.save(new Pet (null,"Борис", PetGender.M, PetType.CAT, LocalDate.of(2022,2,11), null, PetStatus.ACTIVE, organizationRepository.findById(73l).orElse(null)));
//        petRepository.save(new Pet(null, "Бакс", PetGender.M, PetType.DOG, LocalDate.of(2018, 4, 17), "лайки", PetStatus.ACTIVE, organizationRepository.findById(73l).orElse(null)));
//        petRepository.save(new Pet (null, "Феликс", PetGender.M, PetType.CAT, LocalDate.of(2020, 1, 1), null, PetStatus.ACTIVE, organizationRepository.findById(73l).orElse(null)));
//        petRepository.save(new Pet (null, "Мымра", PetGender.F, PetType.CAT, LocalDate.of(2019, 8, 1), "cфинкса", PetStatus.ACTIVE, organizationRepository.findById(73l).orElse(null)));
    }
}
