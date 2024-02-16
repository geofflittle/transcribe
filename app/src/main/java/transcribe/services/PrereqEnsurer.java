package transcribe.services;

import java.util.concurrent.CompletableFuture;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import transcribe.aws.facades.S3Facade;
import transcribe.config.AppModule;

@Slf4j
@Value
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PrereqEnsurer {

    @Named(AppModule.S3_ROOT_BUCKET_NAME_NAME)
    private final String bucket;

    @NonNull
    private final S3Facade s3;

    public CompletableFuture<Boolean> ensure() {
        return s3.headBucket(bucket)
                .thenCompose(b -> {
                    if (b) {
                        return CompletableFuture.completedFuture(Boolean.TRUE);
                    }
                    return s3.createBucket(bucket)
                            .thenApply(s -> true);
                });
    }
}
