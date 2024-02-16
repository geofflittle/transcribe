package transcribe.model;

import java.nio.file.Path;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nullable;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Wither;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJob;
import transcribe.aws.model.S3ObjectMetadata;
import transcribe.courtsmart.model.CSFile;

@Data
@Builder
public class CourtSmartOggDetails {

    @Wither
    @NonNull
    private final Path path;
    @NonNull
    private final Instant startTime;
    @NonNull
    private final Instant stopTime;
    @Wither
    @Nullable
    private final TranscriptionJob transcriptionJob;
    @Wither
    @Nullable
    private final CompletableFuture<S3ObjectMetadata> transcriptS3ObjectMetaFuture;
    @Wither
    @Nullable
    private final String transcript;

    public static CourtSmartOggDetails fromCourtSmartFile(Path oggPath, CSFile courtSmartFile) {
        return CourtSmartOggDetails.builder()
                .path(oggPath)
                .startTime(Instant.ofEpochSecond(courtSmartFile.getStartTime()))
                .stopTime(Instant.ofEpochMilli(courtSmartFile.getStopTime()))
                .build();
    }

}
