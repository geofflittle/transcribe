package transcribe.aws.facades;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import com.google.inject.Inject;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import transcribe.aws.model.S3ObjectMetadata;

@Slf4j
@Value
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class S3Facade {

    private final S3AsyncClient s3;

    public CompletableFuture<String> createBucket(String bucket) {
        log.info("Will create bucket {}", bucket);
        var request = CreateBucketRequest.builder()
                .bucket(bucket)
                .build();
        return s3.createBucket(request)
                .thenApply(res -> {
                    log.info("Did create bucket {}", bucket);
                    return res.location();
                });
    }

    public CompletableFuture<Boolean> headBucket(String bucket) {
        log.info("Will head bucket {}", bucket);
        var request = HeadBucketRequest.builder()
                .bucket(bucket)
                .build();
        return s3.headBucket(request)
                .handle((res, err) -> {
                    log.info("Did head bucket {}", bucket);
                    if (err != null) {
                        log.info("Bucket is absent, {}", bucket);
                        return false;
                    }
                    log.info("Bucket is present, {}", bucket);
                    return true;
                });
    }

    public CompletableFuture<S3ObjectMetadata> putObject(String bucket, String key, Path path) {
        log.info("Will put obj {} to {}/{}", path, bucket, key);
        var request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        return s3.putObject(request, path)
                .thenApply(res -> {
                    log.info("Did put obj {} to {}/{}", path, bucket, key);
                    return S3ObjectMetadata.builder()
                            .bucket(bucket)
                            .key(key)
                            .build();
                });
    }

    public CompletableFuture<ResponseInputStream<GetObjectResponse>> getObject(String bucket, String key) {
        log.info("Will get stream {}/{}", bucket, key);
        var request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        return s3.getObject(request,
                AsyncResponseTransformer.toBlockingInputStream())
                .thenApply(res -> {
                    log.info("Did get stream {}/{}", bucket, key);
                    return res;
                });
    }

}
