package dev.pethaven.mappers;

import dev.pethaven.dto.PetDTO;
import dev.pethaven.dto.SavePet;
import dev.pethaven.entity.Auth;
import dev.pethaven.entity.Organization;
import dev.pethaven.entity.Pet;
import dev.pethaven.entity.PetPhotos;
import dev.pethaven.enums.PetGender;
import dev.pethaven.enums.PetStatus;
import dev.pethaven.enums.PetType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-28T13:57:09+0300",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 20.0.1 (Oracle Corporation)"
)
public class PetMapperImpl implements PetMapper {

    @Override
    public PetDTO toDTO(Pet pet) {
        if ( pet == null ) {
            return null;
        }

        PetDTO petDTO = new PetDTO();

        petDTO.setPhotoRefs( petPhotosListToStringList( pet.getPetPhotos() ) );
        petDTO.setCity( petOrganizationCity( pet ) );
        petDTO.setNameOrganization( petOrganizationNameOrganization( pet ) );
        petDTO.setUsernameOrganization( petOrganizationAuthUsername( pet ) );
        petDTO.setId( pet.getId() );
        petDTO.setName( pet.getName() );
        if ( pet.getGender() != null ) {
            petDTO.setGender( pet.getGender().name() );
        }
        if ( pet.getTypePet() != null ) {
            petDTO.setTypePet( pet.getTypePet().name() );
        }
        if ( pet.getBirthDay() != null ) {
            petDTO.setBirthDay( DateTimeFormatter.ISO_LOCAL_DATE.format( pet.getBirthDay() ) );
        }
        petDTO.setBreed( pet.getBreed() );
        petDTO.setDescription( pet.getDescription() );
        if ( pet.getStatus() != null ) {
            petDTO.setStatus( pet.getStatus().name() );
        }

        return petDTO;
    }

    @Override
    public Pet toEntity(PetDTO petDTO) {
        if ( petDTO == null ) {
            return null;
        }

        Pet pet = new Pet();

        pet.setId( petDTO.getId() );
        pet.setName( petDTO.getName() );
        if ( petDTO.getGender() != null ) {
            pet.setGender( Enum.valueOf( PetGender.class, petDTO.getGender() ) );
        }
        if ( petDTO.getTypePet() != null ) {
            pet.setTypePet( Enum.valueOf( PetType.class, petDTO.getTypePet() ) );
        }
        if ( petDTO.getBirthDay() != null ) {
            pet.setBirthDay( LocalDate.parse( petDTO.getBirthDay() ) );
        }
        pet.setBreed( petDTO.getBreed() );
        pet.setDescription( petDTO.getDescription() );
        if ( petDTO.getStatus() != null ) {
            pet.setStatus( Enum.valueOf( PetStatus.class, petDTO.getStatus() ) );
        }

        return pet;
    }

    @Override
    public Pet toEntity(SavePet newPetInfo) {
        if ( newPetInfo == null ) {
            return null;
        }

        Pet pet = new Pet();

        pet.setName( newPetInfo.getName() );
        if ( newPetInfo.getGender() != null ) {
            pet.setGender( Enum.valueOf( PetGender.class, newPetInfo.getGender() ) );
        }
        if ( newPetInfo.getTypePet() != null ) {
            pet.setTypePet( Enum.valueOf( PetType.class, newPetInfo.getTypePet() ) );
        }
        if ( newPetInfo.getBirthDay() != null ) {
            pet.setBirthDay( LocalDate.parse( newPetInfo.getBirthDay() ) );
        }
        pet.setBreed( newPetInfo.getBreed() );
        pet.setDescription( newPetInfo.getDescription() );

        return pet;
    }

    @Override
    public void updatePet(SavePet petDTO, Pet pet) {
        if ( petDTO == null ) {
            return;
        }

        pet.setName( petDTO.getName() );
        if ( petDTO.getGender() != null ) {
            pet.setGender( Enum.valueOf( PetGender.class, petDTO.getGender() ) );
        }
        else {
            pet.setGender( null );
        }
        if ( petDTO.getTypePet() != null ) {
            pet.setTypePet( Enum.valueOf( PetType.class, petDTO.getTypePet() ) );
        }
        else {
            pet.setTypePet( null );
        }
        if ( petDTO.getBirthDay() != null ) {
            pet.setBirthDay( LocalDate.parse( petDTO.getBirthDay() ) );
        }
        else {
            pet.setBirthDay( null );
        }
        pet.setBreed( petDTO.getBreed() );
        pet.setDescription( petDTO.getDescription() );
    }

    protected List<String> petPhotosListToStringList(List<PetPhotos> list) {
        if ( list == null ) {
            return null;
        }

        List<String> list1 = new ArrayList<String>( list.size() );
        for ( PetPhotos petPhotos : list ) {
            list1.add( mapPetPhotosToString( petPhotos ) );
        }

        return list1;
    }

    private String petOrganizationCity(Pet pet) {
        if ( pet == null ) {
            return null;
        }
        Organization organization = pet.getOrganization();
        if ( organization == null ) {
            return null;
        }
        String city = organization.getCity();
        if ( city == null ) {
            return null;
        }
        return city;
    }

    private String petOrganizationNameOrganization(Pet pet) {
        if ( pet == null ) {
            return null;
        }
        Organization organization = pet.getOrganization();
        if ( organization == null ) {
            return null;
        }
        String nameOrganization = organization.getNameOrganization();
        if ( nameOrganization == null ) {
            return null;
        }
        return nameOrganization;
    }

    private String petOrganizationAuthUsername(Pet pet) {
        if ( pet == null ) {
            return null;
        }
        Organization organization = pet.getOrganization();
        if ( organization == null ) {
            return null;
        }
        Auth auth = organization.getAuth();
        if ( auth == null ) {
            return null;
        }
        String username = auth.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }
}
