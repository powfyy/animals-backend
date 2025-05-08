package dev.animals;

import dev.animals.entity.*;
import dev.animals.enums.AnimalStatus;
import dev.animals.enums.GenderType;
import dev.animals.enums.Role;
import dev.animals.repository.ChatRepository;
import dev.animals.repository.OrganizationRepository;
import dev.animals.repository.UserRepository;
import dev.animals.repository.animal.AnimalRepository;
import dev.animals.repository.animal.AnimalTypeRepository;
import dev.animals.repository.attribute.AttributeRepository;
import dev.animals.service.AttributeService;
import dev.animals.service.OrganizationService;
import dev.animals.service.UserService;
import dev.animals.service.animal.AnimalService;
import dev.animals.service.animal.AnimalTypeService;
import dev.animals.web.dto.AttributeDto;
import dev.animals.web.dto.SignupUserRequest;
import dev.animals.web.dto.animal.AnimalDto;
import dev.animals.web.dto.animal.AnimalSaveDto;
import dev.animals.web.dto.animal.AnimalTypeDto;
import dev.animals.web.dto.organization.SignupOrganizationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.time.LocalDate;
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

  private final AnimalRepository animalRepository;
  private final AnimalService animalService;

  private final UserRepository userRepository;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  private final ChatRepository chatRepository;

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

    if (animalRepository.count() == 0) {
      AnimalSaveDto dto = new AnimalSaveDto();
      dto.setName("Кеша");
      dto.setGender(GenderType.M);
      dto.setType("Попугай");
      dto.setBirthDay(LocalDate.now().minusDays(15));
      dto.setBreed("Волнистый");
      dto.setDescription("Самый красивый говорун");
      dto.setOrganizationUsername("myanimals");
      dto.setAttributes(Map.of("окрас", "голубой"));
      AnimalDto saved = animalService.create(dto);
      animalService.update(toSaveDto(saved));

      animalService.savePhoto(saved.getId(), getResourceFile("/initializer/kesha.png"));
      animalService.savePhoto(saved.getId(), getResourceFile("/initializer/kesha2.png"));
    }

    if (userRepository.count() == 0) {
      SignupUserRequest request = new SignupUserRequest();
      request.setUsername("user");
      request.setPassword("1234");
      request.setName("Глеб");
      request.setLastname("Акимочкин");
      request.setPhoneNumber("79003332211");
      userService.create(request);
    }

    if (!userRepository.existsByAuthRole(Role.ADMIN)) {
      AuthEntity auth = new AuthEntity("admin", Role.ADMIN, passwordEncoder.encode("admin"));
      UserEntity userEntity = new UserEntity();
      userEntity.setName("Admin");
      userEntity.setLastname("Admin");
      userEntity.setPhoneNumber("70000000000");
      userEntity.setAuth(auth);
      userRepository.save(userEntity);
    }

    if (chatRepository.count() == 0) {
      ChatEntity chat = new ChatEntity();
      UserEntity user = userService.findByUsername("user");
      chat.setUser(user);
      OrganizationEntity organization = organizationService.findByUsername("myanimals");
      chat.setOrganization(organization);
      chat = chatRepository.save(chat);
      MessageEntity message = new MessageEntity();
      message.setUser(user);
      message.setMessage("Приветствую!");
      message.setChat(chat);
      chat.getMessages().add(message);
      chatRepository.save(chat);
    }
  }

  private AnimalSaveDto toSaveDto(AnimalDto animalDto) {
    AnimalSaveDto saveDto = new AnimalSaveDto();
    saveDto.setId(animalDto.getId());
    saveDto.setName(animalDto.getName());
    saveDto.setGender(animalDto.getGender());
    saveDto.setType(animalDto.getType());
    saveDto.setBirthDay(LocalDate.parse(animalDto.getBirthDay()));
    saveDto.setBreed(animalDto.getBreed());
    saveDto.setStatus(AnimalStatus.ACTIVE);
    saveDto.setDescription(animalDto.getDescription());
    saveDto.setOrganizationUsername(animalDto.getOrganizationUsername());
    saveDto.setAttributes(animalDto.getAttributes());
    return saveDto;
  }

  public MultipartFile getResourceFile(String pathInResources) {
    try {
      ClassPathResource resource = new ClassPathResource(pathInResources);
      byte[] content = resource.getInputStream().readAllBytes();

      return new MultipartFile() {
        @Override
        public String getName() {
          return resource.getFilename();
        }

        @Override
        public String getOriginalFilename() {
          return resource.getFilename();
        }

        @Override
        public String getContentType() {
          return "image/png";
        }

        @Override
        public boolean isEmpty() {
          return content.length == 0;
        }

        @Override
        public long getSize() {
          return content.length;
        }

        @Override
        public byte[] getBytes() {
          return content;
        }

        @Override
        public InputStream getInputStream() {
          return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
          try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(content);
          }
        }
      };
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to read resource file: " + pathInResources, e);
    }
  }

}
