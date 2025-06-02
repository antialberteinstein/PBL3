package dut.gianguhohi.shoppiefood.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    private static final String UPLOAD_DIR = "uploads";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file selected");
        }
        try {
            // Create upload directory if not exists
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Clean file name
            String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = Paths.get(UPLOAD_DIR, fileName);

            // Save file
            Files.copy(file.getInputStream(), filePath);

            // Return relative path
            String imagePath = "/" + UPLOAD_DIR + "/" + fileName;
            return ResponseEntity.ok().body(imagePath);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Could not upload file: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteImage(@RequestParam("path") String path) {
        // Only allow deletion inside the uploads directory for safety
        if (path == null || !path.startsWith("/uploads/")) {
            return ResponseEntity.badRequest().body("Invalid path");
        }
        try {
            // Remove leading slash if present
            String relativePath = path.startsWith("/") ? path.substring(1) : path;
            File file = new File(relativePath);
            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    return ResponseEntity.ok().body("Deleted successfully");
                } else {
                    return ResponseEntity.status(500).body("Could not delete file");
                }
            } else {
                return ResponseEntity.status(404).body("File not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}