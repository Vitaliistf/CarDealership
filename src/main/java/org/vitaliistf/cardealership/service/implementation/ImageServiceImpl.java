package org.vitaliistf.cardealership.service.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vitaliistf.cardealership.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Implementation of {@link ImageService} for handling image upload, retrieval, and deletion.
 */
@Service
public class ImageServiceImpl implements ImageService {

    private final Path uploadDirectory;

    /**
     * Constructs an ImageServiceImpl object with the specified upload directory.
     *
     * @param uploadDirectoryPath Upload directory
     */
    public ImageServiceImpl(@Value("${image.upload.directory}") String uploadDirectoryPath) {
        this.uploadDirectory = Paths.get(uploadDirectoryPath);
    }

    /**
     * Saves the uploaded image to the file system.
     *
     * @param image MultipartFile representing the uploaded image
     * @return String representing the filename of the saved image
     */
    @Override
    public String saveImage(MultipartFile image) {
        if (image.isEmpty()) {
            return "";
        }

        String photoName = generatePhotoName(image.getOriginalFilename());

        try {
            Path imagePath = this.uploadDirectory.resolve(photoName);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image.", e);
        }

        return photoName;
    }

    /**
     * Generates file name for photo.
     *
     * @param originalFilename Original file name
     * @return byte array representing the image data
     */
    private String generatePhotoName(String originalFilename) {
        return System.currentTimeMillis() + "_" + originalFilename;
    }

    /**
     * Retrieves the image bytes from the file system based on the provided URL.
     *
     * @param url String representing the URL of the image
     * @return byte array representing the image data
     */
    @Override
    public byte[] getImage(String url) {
        Path imagePath = uploadDirectory.resolve(url);

        try {
            return Files.readAllBytes(imagePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image.", e);
        }
    }

    /**
     * Deletes the image from the file system based on the provided URL.
     *
     * @param url String representing the URL of the image to be deleted
     */
    @Override
    public void deleteImage(String url) {
        Path imagePath = uploadDirectory.resolve(url);

        try {
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image.", e);
        }
    }

}
