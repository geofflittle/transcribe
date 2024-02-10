package transcribe.aws.model.transcribe;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Transcripts {
    private final List<TranscriptResult> transcripts;
    private final List<TranscriptItem> items;
}
