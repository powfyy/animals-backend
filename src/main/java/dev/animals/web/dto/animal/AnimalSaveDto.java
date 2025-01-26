package dev.animals.web.dto.animal;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class AnimalSaveDto {

    @Size(min = 2, message = "name pet must be minimum 2 chars")
    private String name;
    @Size(min = 1, max = 1, message = "gender must be 1 char")
    private String gender;
    @Size(min = 3, max = 3, message = "type pet must be 3 chars")
    private String typePet;
    @NotNull(message = "birthday is mandatory") @NotBlank(message = "birthday cannot be empty")
    private String birthDay;
    private String breed;
    private String description;
    private List <String> deletedPhotoRefs = new ArrayList<>();
    private ArrayList<MultipartFile> files = new ArrayList<>();
}
