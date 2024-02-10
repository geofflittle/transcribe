package transcribe.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class AwsTranscriptAlternative {
    private final String confidence;
    private final String content;
}
