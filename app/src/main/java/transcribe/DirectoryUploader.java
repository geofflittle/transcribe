package transcribe;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Value
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DirectoryUploader {

    private final S3Facade s3;

    public List<String> upload(File directoryFile, String s3BucketName) {
        log.info("Uploading " + directoryFile + " to " + s3BucketName);
        if (!directoryFile.exists() || !directoryFile.isDirectory()) {
            throw new RuntimeException("No directory with path " + directoryFile);
        }
        if (!s3.headBucket(s3BucketName)) {
            log.info("No head bucket {}", s3BucketName);
            s3.createBucket(s3BucketName);
        }
        List<String> s3Uris = Arrays.stream(directoryFile.listFiles())
                .map(file -> s3.putObject(s3BucketName, file))
                .collect(Collectors.toList());
        return s3Uris;
    }
}
