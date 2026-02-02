package sparta.cloudassignment.service;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sparta.cloudassignment.exception.UploadException;
import sparta.cloudassignment.repository.MemberRepository;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class S3Service {

    private final MemberRepository memberRepository;

    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofDays(7);

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public void upload(Long id, MultipartFile file) {
        try {
            String key = "uploads/" + UUID.randomUUID().toString().substring(0,10) + "_" + file.getOriginalFilename();
            s3Template.upload(bucket, key, file.getInputStream());
            memberRepository.updateFileUrl(key, id);
        } catch (IOException e) {
            throw new UploadException("Error while uploading file", e);
        }
    }

    public URL getDownloadUrl(Long id) {
        String key = memberRepository.getFileUrl(id);
        return s3Template.createSignedGetURL(bucket, key, PRESIGNED_URL_EXPIRATION);
    }
}
