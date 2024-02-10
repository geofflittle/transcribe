package transcribe.services;

import java.io.File;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import transcribe.aws.facades.S3Facade;
import transcribe.aws.model.S3ObjectMetadata;
import transcribe.config.AppModule;

@Slf4j
@Value
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class S3FileUploader {

    @Named(AppModule.S3_ROOT_BUCKET_NAME_NAME)
    private final String bucket;

    private final S3Facade s3;

    public S3ObjectMetadata upload(String jobName, File file) {
        String key = String.format("%s/%s", jobName, file.getName());
        S3ObjectMetadata objectMD = s3.putObject(bucket, key, file);
        return objectMD;
    }

}
