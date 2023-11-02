package dev.pethaven.pojo;

import dev.pethaven.entity.PetPhotos;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class SavePet {
    private String name;
    private String gender;
    private String typePet;
    private String birthDay;
    private String breed;
    private String description;
    private List <String> deletedPhotoRefs = new ArrayList<>();
    private ArrayList<MultipartFile> files = new ArrayList<>();
}
