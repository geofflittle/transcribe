package transcribe.services;

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

    public boolean ensure() {
        if (s3.headBucket(bucket).join()) {
            log.info("Bucket {} is present", bucket);
            return true;
        }
        log.info("Bucket {} is absent", bucket);
        s3.createBucket(bucket).join();
        return true;
    }
}
