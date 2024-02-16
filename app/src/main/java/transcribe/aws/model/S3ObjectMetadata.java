package transcribe.aws.model;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.Builder;
import lombok.SneakyThrows;
import lombok.Value;

@Value
@Builder
public class S3ObjectMetadata {
    private final String bucket;
    private final String key;

    public String toS3Uri() {
        return String.format("s3://%s/%s", bucket, key);
    }

    @SneakyThrows
    public static S3ObjectMetadata fromS3Uri(String s3Uri) {
        URI uri = new URI(s3Uri);
        String bucket = uri.getHost();
        String key = uri.getPath().substring(1);
        return S3ObjectMetadata.builder()
                .bucket(bucket)
                .key(key)
                .build();
    }

    @SneakyThrows
    public static S3ObjectMetadata fromFileUri(String fileUri) {
        URI uri = new URI(fileUri);
        Path path = Paths.get(uri.getPath());
        String bucket = path.getName(0).toString();
        String key = path.subpath(1, path.getNameCount()).toString();
        return S3ObjectMetadata.builder()
                .bucket(bucket)
                .key(key)
                .build();
    }
}
