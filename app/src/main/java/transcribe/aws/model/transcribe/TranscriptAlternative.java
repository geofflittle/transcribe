package transcribe.aws.model.transcribe;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class TranscriptAlternative {
    private final String confidence;
    private final String content;
}
