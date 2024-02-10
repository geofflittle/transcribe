package transcribe.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class AwsTranscriptItem {
    private final String type;
    private final List<AwsTranscriptAlternative> alternatives;
    private final String start_time;
    private final String end_time;
}
