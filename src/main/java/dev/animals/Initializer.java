package dev.animals;

import dev.animals.repository.OrganizationRepository;
import dev.animals.repository.animal.AnimalTypeRepository;
import dev.animals.repository.attribute.AttributeRepository;
import dev.animals.service.AttributeService;
import dev.animals.service.OrganizationService;
import dev.animals.service.animal.AnimalTypeService;
import dev.animals.web.dto.AttributeDto;
import dev.animals.web.dto.SignupOrganizationRequest;
import dev.animals.web.dto.animal.AnimalTypeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Initializer {

  private final AttributeRepository attributeRepository;
  private final AttributeService attributeService;

  private final AnimalTypeRepository animalTypeRepository;
  private final AnimalTypeService animalTypeService;

  private final OrganizationRepository organizationRepository;
  private final OrganizationService organizationService;

  @Transactional
  public void initial() {
    if (attributeRepository.count() == 0) {
      AttributeDto firstAttribute = new AttributeDto();
      firstAttribute.setName("окрас");
      firstAttribute.setPriority(1);
      firstAttribute.setValues(Set.of("белый", "синий", "зеленый", "красный", "серый", "желтый", "черный", "коричневый", "рыжий", "голубой"));
      attributeService.save(firstAttribute);

      AttributeDto secondAttribute = new AttributeDto();
      secondAttribute.setName("Длина шерсти");
      secondAttribute.setPriority(1);
      secondAttribute.setValues(Set.of("длинная", "средняя", "короткая"));
      attributeService.save(secondAttribute);
    }
    if (animalTypeRepository.count() == 0) {
      AnimalTypeDto animalTypeDto = new AnimalTypeDto();
      animalTypeDto.setName("Попугай");
      animalTypeDto.setPriority(1);
      animalTypeDto.setAttributes(Map.of(
        "окрас", Set.of("синий", "красный", "зеленый", "желтый", "белый", "голубой", "серый")
      ));
      animalTypeService.save(animalTypeDto);
    }
    if (organizationRepository.count() == 0) {
      SignupOrganizationRequest organizationRequest = new SignupOrganizationRequest();
      organizationRequest.setUsername("myanimals");
      organizationRequest.setPassword("myanimals");
      organizationRequest.setName("My Animals");
      organizationRequest.setCity("Воронеж");
      organizationRequest.setPassportSeries("2016");
      organizationRequest.setPassportNumber("123431");
      organizationRequest.setPhoneNumber("71119123344");

      organizationService.create(organizationRequest);
    }

  }

}
