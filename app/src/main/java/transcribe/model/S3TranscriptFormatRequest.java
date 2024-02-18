package transcribe.model;

import java.nio.file.Path;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import transcribe.aws.model.S3ObjectMetadata;

@Data
@Builder
public class S3TranscriptFormatRequest {
    @NonNull
    private final S3ObjectMetadata transcriptS3ObjectMeta;
    @NonNull
    private final Path transcriptOutputDir;
    @NonNull
    private final String transcriptDocTitle;
    @NonNull
    private final String caseName;
    @NonNull
    private final String audioFilename;
}
