package dev.animals.service;

import dev.animals.exception.LogicException;
import dev.animals.exception.helper.CommonErrorCode;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

  private final MinioClient minioClient;

  private final String BUCKET_PREFIX = "animal-";
  private final String POLICY_PATH = "minio/policy.json";
  private final String POLICY_BUCKET_NAME_KEY = "BUCKET_NAME_KEY";

  public void createBucket(String animalId) {
    try {
      String bucketName = BUCKET_PREFIX + animalId;
      if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
        String policyJson = readPolicy();
        policyJson = policyJson.replace(POLICY_BUCKET_NAME_KEY, bucketName);
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policyJson).build());
        log.info("bucket successfully created.");
      }
    } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Ошибка при создании бакета: " + e.getMessage());
    }
  }

  private String readPolicy() {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(POLICY_PATH)) {
      if (Objects.isNull(inputStream)) {
        throw new LogicException(CommonErrorCode.JAVA_ERROR, "Файл policy не найден: " + POLICY_PATH);
      }
      return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (Exception ex) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Ошибка при чтении policy: " + ex.getMessage());
    }
  }

  public void uploadFile(List<MultipartFile> files, String bucketName) {
    files.forEach(file -> {
      try {
        minioClient.putObject(PutObjectArgs.builder()
          .bucket(BUCKET_PREFIX + bucketName)
          .object(file.getOriginalFilename())
          .stream(file.getInputStream(), file.getSize(), -1)
          .contentType(file.getContentType())
          .build());
      } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
        throw new LogicException(CommonErrorCode.JAVA_ERROR, "Ошибка при загрузке файла: " + e.getMessage());
      }
    });
  }

  public boolean bucketExists(String name) {
    if (StringUtils.isBlank(name)) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Невозможно проверить существование бакета: передано пустое название");
    }
    try {
      return minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_PREFIX + name).build());
    } catch (Exception ex) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, ex.getMessage());
    }
  }

  public void removeBucket(String bucketName) {
    try {
      minioClient.removeBucket(RemoveBucketArgs.builder().bucket(BUCKET_PREFIX + bucketName).build());
    } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | MinioException e) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Ошибка при удалении бакета: " + e.getMessage());
    }
  }

  public void removeFiles(List<String> photoRefs, String bucketName) {
    try {
      List<DeleteObject> objects = photoRefs.stream()
        .map(DeleteObject::new)
        .collect(Collectors.toList());
      Iterable<Result<DeleteError>> results =
        minioClient.removeObjects(
          RemoveObjectsArgs.builder().bucket(BUCKET_PREFIX + bucketName).objects(objects).build());
      for (Result<DeleteError> result : results) {
        DeleteError error = result.get();
        throw new LogicException(CommonErrorCode.JAVA_ERROR, "Ошибка при удалении объектов:" + error.objectName());
      }
    } catch (InvalidKeyException | MinioException | IOException | NoSuchAlgorithmException e) {
      throw new LogicException(CommonErrorCode.JAVA_ERROR, "Ошибка при удалении файлов: " + e.getMessage());
    }
  }
}
