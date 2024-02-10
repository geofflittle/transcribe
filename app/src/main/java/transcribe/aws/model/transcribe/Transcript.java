package transcribe.aws.model.transcribe;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Transcript {
    private final String jobName;
    private final String accountId;
    private final String status;
    private final Transcripts results;
}
