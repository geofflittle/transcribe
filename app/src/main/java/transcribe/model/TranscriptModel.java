package transcribe.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TranscriptModel {
    private final String documentTitle;
    private final String caseName;
    private final String info;
    private final String filename;
    private final String body;
}
