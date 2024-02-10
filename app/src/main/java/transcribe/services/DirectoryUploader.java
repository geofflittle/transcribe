package transcribe.services;

import java.io.File;
import java.util.List;

import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import transcribe.aws.facades.S3Facade;

@Slf4j
@Value
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DirectoryUploader {

    private final S3Facade s3;

    public List<String> upload(String s3Bucket, String s3KeyPrefix, File uploadDir) {
        // if (!uploadDir.exists() || !uploadDir.isDirectory()) {
        // throw new RuntimeException("No directory with path " + uploadDir);
        // }
        // if (!s3.headBucket(s3Bucket)) {
        // log.info("No head bucket {}", s3Bucket);
        // s3.createBucket(s3Bucket);
        // }
        // List<String> s3Uris = Arrays.stream(uploadDir.listFiles())
        // .map(file -> s3.putObject(s3Bucket, s3KeyPrefix + "/" + file.getName(),
        // file))
        // .collect(Collectors.toList());
        // return s3Uris;
        return null;
    }
}
