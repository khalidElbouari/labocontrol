package org.khalid.labocontrol.service.security;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class PictureService {

    private static final String UPLOAD_DIR = "src/main/resources/static/images";

    public String saveProfilePicture(MultipartFile profilePicture) throws IOException {
        String fileName = generateUniqueFileName(profilePicture.getOriginalFilename());
        String relativePath = "images/" + fileName;

        // Ensure the upload directory exists
        createUploadDirectory();

        // Save the file to the static/images folder
        Path targetPath = Path.of(UPLOAD_DIR, fileName);
        Files.copy(profilePicture.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return relativePath;
    }

    private void createUploadDirectory() {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = StringUtils.getFilenameExtension(originalFileName);
        String baseName = StringUtils.stripFilenameExtension(originalFileName);

        String uniqueFileName = baseName + "_" + UUID.randomUUID().toString() + "." + extension;

        return uniqueFileName;
    }
}
