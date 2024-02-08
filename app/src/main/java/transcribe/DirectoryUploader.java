package transcribe;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.Inject;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Value
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DirectoryUploader {

    private final S3Client s3;

    private String createBucket(String s3BucketName) {
        log.info("Will create bucket {}", s3BucketName);
        CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                .bucket(s3BucketName)
                .build();
        CreateBucketResponse createBucketResponse = s3.createBucket(createBucketRequest);
        log.info("Did create bucket {}", s3BucketName);
        return createBucketResponse.location();
    }

    private boolean headBucket(String s3BucketName) {
        try {
            log.info("Will head bucket {}", s3BucketName);
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(s3BucketName)
                    .build();
            s3.headBucket(headBucketRequest);
            log.info("Did head bucket {}", s3BucketName);
        } catch (Throwable t) {
            return false;
        }
        return true;
    }

    private String putObject(String s3BucketName, File file) {
        log.info("Will put obj request {}", file);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3BucketName)
                .key(file.getName())
                .build();
        s3.putObject(putObjectRequest, file.toPath());
        log.info("Did put obj request {}", file);
        return String.format("s3://%s/%s", s3BucketName, file.getName());
    }

    public List<String> upload(File directoryFile, String s3BucketName) throws URISyntaxException, IOException {
        log.info("Uploading " + directoryFile + " to " + s3BucketName);
        if (!directoryFile.exists() || !directoryFile.isDirectory()) {
            throw new RuntimeException("No directory with path " + directoryFile);
        }
        if (!headBucket(s3BucketName)) {
            log.info("No head bucket {}", s3BucketName);
            createBucket(s3BucketName);
        }
        List<String> s3Uris = Arrays.stream(directoryFile.listFiles())
                .map(file -> putObject(s3BucketName, file))
                .collect(Collectors.toList());
        return s3Uris;
    }
}
