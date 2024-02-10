package transcribe;

import java.io.File;

import com.google.inject.Inject;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Value
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class S3Facade {

    private final S3Client s3;

    public String createBucket(String bucket) {
        log.info("Will create bucket {}", bucket);
        CreateBucketRequest request = CreateBucketRequest.builder()
                .bucket(bucket)
                .build();
        CreateBucketResponse response = s3.createBucket(request);
        log.info("Did create bucket {}", bucket);
        return response.location();
    }

    public boolean headBucket(String bucket) {
        try {
            log.info("Will head bucket {}", bucket);
            HeadBucketRequest request = HeadBucketRequest.builder()
                    .bucket(bucket)
                    .build();
            s3.headBucket(request);
            log.info("Did head bucket {}", bucket);
        } catch (Throwable t) {
            log.info("Bucket is absent, {}", bucket);
            return false;
        }
        log.info("Bucket is present, {}", bucket);
        return true;
    }

    public String putObject(String bucket, String key, File file) {
        log.info("Will put obj {} to {}/{}", file, bucket, key);
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3.putObject(request, file.toPath());
        log.info("Did put obj {} to {}/{}", file, bucket, key);
        String s3Uri = String.format("s3://%s/%s", bucket, key);
        log.info("S3 URI {}", s3Uri);
        return s3Uri;
    }

    public ResponseInputStream<GetObjectResponse> getObject(String bucket, String key) {
        log.info("Will get stream {}/{}", bucket, key);
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        ResponseInputStream<GetObjectResponse> response = s3.getObject(request);
        log.info("Did get stream {}/{}", bucket, key);
        return response;
    }

}
