package sparta.cloudassignment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sparta.cloudassignment.dto.FileDownloadUrlResponse;
import sparta.cloudassignment.service.S3Service;

import java.net.URL;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final S3Service s3Service;

    @PostMapping("/api/members/{id}/profile-image")
    public ResponseEntity<String> upload(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        s3Service.upload(id, file);
        return ResponseEntity.ok("File uploaded");
    }

    @GetMapping("/api/members/{id}/profile-image")
    public ResponseEntity<FileDownloadUrlResponse> getDownloadUrl(@PathVariable Long id) {
        URL url = s3Service.getDownloadUrl(id);
        return ResponseEntity.ok(new FileDownloadUrlResponse(url.toString()));
    }
}
