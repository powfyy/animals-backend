package dev.animals.service.animal;

import dev.animals.entity.animal.AnimalEntity;
import dev.animals.entity.animal.AnimalPhotosEntity;
import dev.animals.exception.LogicException;
import dev.animals.exception.helper.CommonErrorCode;
import dev.animals.repository.animal.AnimalPhotosRepository;
import dev.animals.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalPhotoService {

  private final AnimalPhotosRepository repository;
  private final MinioService minioService;

  public void save(AnimalEntity animal, MultipartFile photo) {
    String bucketName = animal.getId().toString();
    if (!minioService.bucketExists(bucketName)) {
      minioService.createBucket(bucketName);
    }
    minioService.uploadFile(List.of(photo), bucketName);
    repository.save(new AnimalPhotosEntity(photo.getOriginalFilename(), animal));
  }

  public void remove(String animalId, String fileName) {
    minioService.removeFiles(List.of(fileName), animalId);
    repository.deleteByPhotoRef(fileName);
  }

  public void removeBucket(List<String> fileNames, String animalId) {
    if (StringUtils.isBlank(animalId)) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Невозможно удалить бакет животного: передан пустой id");
    }
    if (!fileNames.isEmpty()) {
      minioService.removeFiles(fileNames, animalId);
      repository.deleteAllByPhotoRefIn(fileNames);
    }
    minioService.removeBucket(animalId);
  }
}
