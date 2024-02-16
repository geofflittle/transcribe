package transcribe.model;

import java.nio.file.Path;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class CourtSmartTranscribeRequest {
    @NonNull
    private final Path audioFile;
    @NonNull
    private final LocalDateTime recordingStart;
    @NonNull
    private final Path transcriptOutputDir;
    @NonNull
    private final String transcriptDocTitle;
    @NonNull
    private final String caseName;
}
