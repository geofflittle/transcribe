package transcribe;

import java.io.File;

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
public class S3Facade {

    private final S3Client s3;

    public String createBucket(String bucketName) {
        log.info("Will create bucket {}", bucketName);
        CreateBucketRequest request = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();
        CreateBucketResponse response = s3.createBucket(request);
        log.info("Did create bucket {}", bucketName);
        return response.location();
    }

    public boolean headBucket(String bucketName) {
        try {
            log.info("Will head bucket {}", bucketName);
            HeadBucketRequest request = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3.headBucket(request);
            log.info("Did head bucket {}", bucketName);
        } catch (Throwable t) {
            log.info("Bucket is absent, {}", bucketName);
            return false;
        }
        log.info("Bucket is present, {}", bucketName);
        return true;
    }

    public String putObject(String bucketName, File file) {
        log.info("Will put obj {}", file);
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getName())
                .build();
        s3.putObject(request, file.toPath());
        log.info("Did put obj {}", file);
        String s3Uri = String.format("s3://%s/%s", bucketName, file.getName());
        log.info("S3 URI {}", s3Uri);
        return s3Uri;
    }

}
