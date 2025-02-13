package com.management_system.utilities.utils;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.management_system.utilities.config.meta_data.SystemConfigKeyName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Slf4j
@RequiredArgsConstructor
@Service
public class FirebaseUtils {
    final SystemConfigEnvUtils credentialsUtils;

    private String getUploadFileUrl(File file, String fileName) throws IOException {
        try {
            String firebaseStorageBucketName = credentialsUtils.getCredentials(SystemConfigKeyName.FIREBASE_STORAGE_BUCKET_NAME);

            BlobId blobId = BlobId.of(firebaseStorageBucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

            InputStream inputStream = FirebaseUtils.class.getClassLoader().getResourceAsStream(credentialsUtils.getCredentials(SystemConfigKeyName.FIREBASE_PRIVATE_KEY_FILE_PATH));
            Credentials credentials = GoogleCredentials.fromStream(inputStream);

            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            storage.create(blobInfo, Files.readAllBytes(file.toPath()));

            String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/" + firebaseStorageBucketName + "/o/%s?alt=media";

            return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        }
        catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }


    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }


    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    public String upload(MultipartFile multipartFile, String name) {
        try {
            // get original file name
            String fileName = (name == null || name.isBlank())
                    ? multipartFile.getOriginalFilename()
                    : name;

            // convert multipartFile to File
            File file = this.convertToFile(multipartFile, fileName);
            // get uploaded file link
            String URL = this.getUploadFileUrl(file, fileName);
            file.delete();

            return URL;
        } catch (Exception e) {
            e.printStackTrace();

            return "Error to upload file to Firebase storage";
        }
    }

    public boolean delete(String fileName) {
        try {
            if(fileName == null || fileName.isBlank()) {
                log.error("File name must not be null");

                return false;
            }

            String firebaseStorageBucketName = credentialsUtils.getCredentials(SystemConfigKeyName.FIREBASE_STORAGE_BUCKET_NAME);
            BlobId blobId = BlobId.of(firebaseStorageBucketName, fileName);

            if(blobId == null) {
                log.error("{} does not exist in Firebase storage", fileName);
            }

            InputStream inputStream = FirebaseUtils.class.getClassLoader()
                    .getResourceAsStream(credentialsUtils.getCredentials(SystemConfigKeyName.FIREBASE_PRIVATE_KEY_FILE_PATH));

            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

            return storage.delete(blobId);
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }
}
