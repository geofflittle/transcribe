package transcribe.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class AwsTranscripts {
    private final List<AwsTranscriptResult> transcripts;
    private final List<AwsTranscriptItem> items;
}
