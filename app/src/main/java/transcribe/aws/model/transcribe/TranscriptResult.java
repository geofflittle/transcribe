package transcribe.aws.model.transcribe;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class TranscriptResult {
    private final String transcript;
}
