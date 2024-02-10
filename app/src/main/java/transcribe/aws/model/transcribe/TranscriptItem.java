package transcribe.aws.model.transcribe;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class TranscriptItem {
    private final String type;
    private final List<TranscriptAlternative> alternatives;
    private final String start_time;
    private final String end_time;
}
