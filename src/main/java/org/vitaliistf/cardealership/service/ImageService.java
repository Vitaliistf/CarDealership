package org.vitaliistf.cardealership.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for handling image upload, retrieval, and deletion.
 */
public interface ImageService {

    /**
     * Saves the uploaded image to the file system.
     *
     * @param image MultipartFile representing the uploaded image
     * @return String representing the filename of the saved image
     */
    String saveImage(MultipartFile image);

    /**
     * Retrieves the image bytes from the file system based on the provided URL.
     *
     * @param url String representing the URL of the image
     * @return byte array representing the image data
     */
    byte[] getImage(String url);

    /**
     * Deletes the image from the file system based on the provided URL.
     *
     * @param url String representing the URL of the image to be deleted
     */
    void deleteImage(String url);

}
