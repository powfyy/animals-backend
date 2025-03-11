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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AnimalPhotoService {

  private final AnimalPhotosRepository repository;
  private final MinioService minioService;

  public void save(AnimalEntity animal, List<MultipartFile> photos) {
    String bucketName = animal.getId().toString();
    if (!minioService.bucketExists(bucketName)) {
      minioService.createBucket(bucketName);
    }
    if (!photos.isEmpty()) {
      minioService.uploadFile(photos, bucketName);
      repository.saveAll(photos.stream()
        .map(photo -> new AnimalPhotosEntity(photo.getOriginalFilename(), animal))
        .toList());
    }
  }

  public void remove(List<String> fileNames, String animalId) {
    if (Objects.isNull(fileNames)) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR,
        "Невозможно удалить фото животного: переданный список названий файлов равен null");
    }
    if (!fileNames.isEmpty()) {
      minioService.removeFiles(fileNames, animalId);
      repository.deleteAllByPhotoRefIn(fileNames);
    }
  }

  public void removeBucket(List<String> fileNames, String animalId) {
    if (StringUtils.isBlank(animalId)) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Невозможно удалить бакет животного: передан пустой id");
    }
    if (!fileNames.isEmpty()) {
      remove(fileNames, animalId);
    }
    minioService.removeBucket(animalId);
  }
}
