package transcribe.aws.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class S3ObjectMetadata {
    private final String bucket;
    private final String key;

    public String toS3URIString() {
        return String.format("s3://%s/%s", bucket, key);
    }
}
