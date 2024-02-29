package transcribe.aws.model;

import java.net.URI;
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
        var uri = new URI(s3Uri);
        var bucket = uri.getHost();
        var key = uri.getPath().substring(1);
        return S3ObjectMetadata.builder()
                .bucket(bucket)
                .key(key)
                .build();
    }

    @SneakyThrows
    public static S3ObjectMetadata fromFileUri(String fileUri) {
        var uri = new URI(fileUri);
        var path = Paths.get(uri.getPath());
        var bucket = path.getName(0).toString();
        var key = path.subpath(1, path.getNameCount()).toString();
        return S3ObjectMetadata.builder()
                .bucket(bucket)
                .key(key)
                .build();
    }
}
