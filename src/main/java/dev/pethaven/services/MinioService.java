package dev.pethaven.services;

import dev.pethaven.entity.PetPhotos;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.DeleteObject;
import io.minio.messages.DeleteError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MinioService {
    private final MinioClient minioClient;

    @Autowired
    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void createBucket(String bucketName) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                String policyJson = "{ \"Statement\": [ { \"Action\": [ \"s3:GetBucketLocation\", \"s3:ListBucket\" ]," +
                        " \"Effect\": \"Allow\", \"Principal\": \"*\", \"Resource\": \"arn:aws:s3:::" + bucketName + "\" }," +
                        " { \"Action\": \"s3:GetObject\", \"Effect\": \"Allow\", \"Principal\": \"*\"," +
                        " \"Resource\": \"arn:aws:s3:::" + bucketName + "/*\" } ], \"Version\": \"2012-10-17\" }";
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policyJson).build());
                log.info("bucket successfully created.");
            }
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("Error create bucket: {}", e.getMessage());
        }
    }

    public void uploadFile(ArrayList<MultipartFile> files, String bucketName) {
        files.forEach(file -> {
            try {
                InputStream fileInputStream = file.getInputStream();
            } catch (Exception e) {
                log.error("Error: {}", e.getMessage());
            }

            try {
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(file.getOriginalFilename())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
            } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
                log.error("Error upload file: {}", e.getMessage());
            }
        });
    }

    public void removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | MinioException e) {
            log.error("Error remove: bucket {}", e.getMessage());
        }
    }

    public void removeFiles(String bucketName, List<PetPhotos> petPhotos) {
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
            log.error("Error deleting files: {}", e.getMessage());
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
                log.error("Error in deleting object {}; {}", error.objectName(), error.message());
            }
        } catch (InvalidKeyException | MinioException | IOException | NoSuchAlgorithmException e) {
            log.error("Error deleting files:{} ", e.getMessage());
        }
    }
}
