package dev.animals.service;

import dev.animals.entity.animal.AnimalPhotosEntity;
import dev.animals.exception.LogicException;
import dev.animals.exception.helper.CommonErrorCode;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

  private final MinioClient minioClient;

  public void createBucket(String bucketName) {
    try {
      boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
      if (!found) {
        String policyJson = new String(Files.readAllBytes(Paths.get("/app/minioPolicy.json")));
        policyJson = policyJson.replace("bucketName", bucketName);
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policyJson).build());
        log.info("bucket successfully created.");
      }
    } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Ошибка при создании бакета: " + e.getMessage());
    }
  }

  public void uploadFile(ArrayList<MultipartFile> files, String bucketName) {
    files.forEach(file -> {
      try {
        minioClient.putObject(PutObjectArgs.builder()
          .bucket(bucketName)
          .object(file.getOriginalFilename())
          .stream(file.getInputStream(), file.getSize(), -1)
          .contentType(file.getContentType())
          .build());
      } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
        throw new LogicException(CommonErrorCode.JAVA_ERROR, "Ошибка при загрузке файла: " + e.getMessage());
      }
    });
  }

  public void removeBucket(String bucketName) {
    try {
      minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | MinioException e) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Ошибка при удалении бакета: " + e.getMessage());
    }
  }

  public void removeFiles(String bucketName, List<AnimalPhotosEntity> petPhotos) {
    try {
      List<DeleteObject> objects = petPhotos.stream()
        .map(petPhoto -> new DeleteObject(petPhoto.getPhotoRef()))
        .collect(Collectors.toList());
      Iterable<Result<DeleteError>> results =
        minioClient.removeObjects(
          RemoveObjectsArgs.builder().bucket(bucketName).objects(objects).build());
      for (Result<DeleteError> result : results) {
        DeleteError error = result.get();
        log.error("Error in deleting object {}; {}", error.objectName(), error.message());
      }
    } catch (InvalidKeyException | MinioException | IOException | NoSuchAlgorithmException e) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Ошибка при удалении файлов: " + e.getMessage());
    }
  }

  public void removeFiles(List<String> photoRefs, String bucketName) {
    try {
      List<DeleteObject> objects = photoRefs.stream()
        .map(DeleteObject::new)
        .collect(Collectors.toList());
      Iterable<Result<DeleteError>> results =
        minioClient.removeObjects(
          RemoveObjectsArgs.builder().bucket(bucketName).objects(objects).build());
      for (Result<DeleteError> result : results) {
        DeleteError error = result.get();
        throw new LogicException(CommonErrorCode.JAVA_ERROR, "Ошибка при удалении объектов:" + error.objectName());
      }
    } catch (InvalidKeyException | MinioException | IOException | NoSuchAlgorithmException e) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Ошибка при удалении файлов: " + e.getMessage());
    }
  }
}
