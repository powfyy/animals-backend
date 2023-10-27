package dev.pethaven.mappers;

import dev.pethaven.dto.PetDTO;
import dev.pethaven.entity.Organization;
import dev.pethaven.entity.Pet;
import dev.pethaven.entity.PetGender;
import dev.pethaven.entity.PetPhotos;
import dev.pethaven.entity.PetStatus;
import dev.pethaven.entity.PetType;
import dev.pethaven.entity.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-27T19:39:21+0300",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.1.1.jar, environment: Java 1.8.0_372 (Amazon.com Inc.)"
)
public class PetMapperImpl implements PetMapper {

    @Override
    public PetDTO toDTO(Pet pet) {
        if ( pet == null ) {
            return null;
        }

        PetDTO petDTO = new PetDTO();

        petDTO.setOrganizationId( petOrganizationId( pet ) );
        petDTO.setUserId( petUserId( pet ) );
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
        if ( pet.getStatus() != null ) {
            petDTO.setStatus( pet.getStatus().name() );
        }
        List<PetPhotos> list = pet.getPetPhotos();
        if ( list != null ) {
            petDTO.setPetPhotos( new ArrayList<PetPhotos>( list ) );
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
        if ( petDTO.getStatus() != null ) {
            pet.setStatus( Enum.valueOf( PetStatus.class, petDTO.getStatus() ) );
        }
        List<PetPhotos> list = petDTO.getPetPhotos();
        if ( list != null ) {
            pet.setPetPhotos( new ArrayList<PetPhotos>( list ) );
        }

        return pet;
    }

    @Override
    public void updatePet(PetDTO petDTO, Pet pet) {
        if ( petDTO == null ) {
            return;
        }

        pet.setId( petDTO.getId() );
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
        if ( petDTO.getStatus() != null ) {
            pet.setStatus( Enum.valueOf( PetStatus.class, petDTO.getStatus() ) );
        }
        else {
            pet.setStatus( null );
        }
        if ( pet.getPetPhotos() != null ) {
            List<PetPhotos> list = petDTO.getPetPhotos();
            if ( list != null ) {
                pet.getPetPhotos().clear();
                pet.getPetPhotos().addAll( list );
            }
            else {
                pet.setPetPhotos( null );
            }
        }
        else {
            List<PetPhotos> list = petDTO.getPetPhotos();
            if ( list != null ) {
                pet.setPetPhotos( new ArrayList<PetPhotos>( list ) );
            }
        }
    }

    private Long petOrganizationId(Pet pet) {
        if ( pet == null ) {
            return null;
        }
        Organization organization = pet.getOrganization();
        if ( organization == null ) {
            return null;
        }
        Long id = organization.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long petUserId(Pet pet) {
        if ( pet == null ) {
            return null;
        }
        User user = pet.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
