package com.mini_drive.drive.minio;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.SetBucketPolicyArgs;
import io.minio.StatObjectArgs;
import io.minio.http.Method;
import io.minio.messages.Item;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MinIOInterfacing {
    @Autowired
    private MinioClient minioClient;

    @Transactional
    public String getUrl(String bucketName, String fileName) throws Exception {
        try {
            if (!(minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(fileName).build())
                    .size() > 0)) {
                return null;
            }
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(60 * 60)
                            .build());
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void uploadFile(String bucketName, String nomeUnico, MultipartFile file) throws Exception {
        try {
            if (!bucketExists(bucketName)) {
                createBucket(bucketName);
            }
            InputStream inputStream = file.getInputStream();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(nomeUnico)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build());
            /* return getUrl(bucketName, nomeUnico); */

        } catch (Exception e) {
            throw e;
        }
    }
    @Transactional
    public String uploadAndGetFile(String bucketName, String nomeUnico, MultipartFile file) throws Exception {
        try {
            if (!bucketExists(bucketName)) {
                createBucket(bucketName);
            }
            InputStream inputStream = file.getInputStream();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(nomeUnico)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build());
            return getUrl(bucketName, nomeUnico);

        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public boolean createBucket(String bucketName) throws Exception {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    // Cria uma "pasta" dentro de um bucket criando um objeto com sufixo "/"
    @Transactional
    public void createFolder(String bucketName, String folderName) throws Exception {
        try {
            if (!bucketExists(bucketName)) {
                createBucket(bucketName);
            }
            // Em MinIO/S3, pastas são representadas por objetos terminados com "/"
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(folderName.endsWith("/") ? folderName : folderName + "/")
                    .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                    .contentType("application/x-directory")
                    .build()
            );
        } catch (Exception e) {
            throw e;
        }
    }


    @Transactional
    public boolean bucketExists(String bucketName) throws Exception {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void deleteFile(String bucketName, String fileName) throws Exception {
        try {
            if (!(minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(fileName).build())
                    .size() > 0)) {
                        log.warn("File does not exist: " + fileName + " in bucket: " + bucketName);
                return;
            }

            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void deleteBucket(String bucketName) throws Exception {
        try {

            Iterable<Result<Item>> objects = minioClient
                    .listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
            for (Result<Item> result : objects) {
                Item item = result.get();
                minioClient
                        .removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(item.objectName()).build());
            }
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void copiarArquivo(String bucketOrigem, String bucketDestino, String nomeArquivo,String nomeCopia) throws Exception {
        try {
            if (!bucketExists(bucketDestino)) {
                createBucket(bucketDestino);
            }
            minioClient.copyObject(
                    io.minio.CopyObjectArgs.builder()
                            .source(io.minio.CopySource.builder()
                                    .bucket(bucketOrigem)
                                    .object(nomeArquivo)
                                    .build())
                            .bucket(bucketDestino)
                            .object(nomeCopia)
                            .build());
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public String salvarProfilePicture(String bucketName, String nomeUnico, byte[] file,String mimetype) throws Exception {
        try {
            if (!bucketExists(bucketName)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket(bucketName)
                        .config("{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":\"*\"},\"Action\":\"s3:GetObject\",\"Resource\":\"arn:aws:s3:::"
                                + bucketName + "/*\"}]}")
                        .build());
            }
            InputStream inputStream = new ByteArrayInputStream(file);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(nomeUnico)
                            .stream(inputStream, file.length, -1)
                            .contentType(mimetype)
                            .build());
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(nomeUnico)
                            .build());
                   

        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public String pegarImagemDePerfil(String bucketName, String nomeUnico) throws Exception {
        try {
            if(!bucketExists(bucketName)){
                return getUrl("defaultpfp","default");
            }
            if (!(minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(nomeUnico).build())
                    .size() > 0)) {
                return getUrl("defaultpfp","default");
            }
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(nomeUnico)
                            .build());
                   
        } catch (Exception e) {
            throw e;
        }
    }


}
