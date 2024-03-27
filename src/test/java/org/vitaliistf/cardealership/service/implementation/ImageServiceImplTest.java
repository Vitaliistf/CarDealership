package org.vitaliistf.cardealership.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplTest {

    private ImageServiceImpl imageService;

    @TempDir
    Path tempDir;

    @Mock
    private MultipartFile mockMultipartFile;

    @BeforeEach
    void setUp() {
        String uploadDirectoryPath = tempDir.toAbsolutePath().toString();
        imageService = new ImageServiceImpl(uploadDirectoryPath);
    }

    @Test
    void testSaveImage() throws IOException {
        byte[] fileContent = "This is a test file content".getBytes();
        when(mockMultipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(mockMultipartFile.getBytes()).thenReturn(fileContent);

        String fileName = imageService.saveImage(mockMultipartFile);
        assertNotNull(fileName);

        Path imagePath = tempDir.resolve(fileName);
        assertTrue(Files.exists(imagePath));
        byte[] savedFileContent = Files.readAllBytes(imagePath);
        assertArrayEquals(fileContent, savedFileContent);
    }

    @Test
    void testSaveImageEmptyFile() {
        when(mockMultipartFile.isEmpty()).thenReturn(true);
        String fileName = imageService.saveImage(mockMultipartFile);
        assertEquals("", fileName);
    }

    @Test
    void testGetImage() throws IOException {
        byte[] fileContent = "This is a test file content".getBytes();
        String fileName = "test.jpg";
        Path imagePath = tempDir.resolve(fileName);
        Files.write(imagePath, fileContent);

        byte[] imageBytes = imageService.getImage(fileName);
        assertArrayEquals(fileContent, imageBytes);
    }

    @Test
    void testDeleteImage() throws IOException {
        byte[] fileContent = "This is a test file content".getBytes();
        String fileName = "test.jpg";
        Path imagePath = tempDir.resolve(fileName);
        Files.write(imagePath, fileContent);

        imageService.deleteImage(fileName);
        assertFalse(Files.exists(imagePath));
    }
}
