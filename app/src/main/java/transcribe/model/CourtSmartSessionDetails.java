package transcribe.model;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Wither;
import transcribe.courtsmart.model.Session;
import transcribe.courtsmart.model.Tag;

@Value
@Builder
public class CourtSmartSessionDetails {

    @NonNull
    private final Instant sessionStartInstant;
    @NonNull
    private final String caseName;
    @NonNull
    private final String detail;
    @NonNull
    private final List<CourtSmartEventDetails> events;
    @Wither
    @NonNull
    private final List<CourtSmartOggDetails> oggDetailss;

    public static CourtSmartSessionDetails fromCourtSmartSession(Map<String, Path> allOggPaths,
            Session courtSmartSession) {
        return CourtSmartSessionDetails.builder()
                .sessionStartInstant(Instant.ofEpochSecond(courtSmartSession.getStartTime()))
                .caseName(courtSmartSession.getCaseInfo())
                .detail(courtSmartSession.getCaption())
                .events(fromCourtSmartTags(courtSmartSession.getTags()))
                .oggDetailss(courtSmartSession.getFiles()
                        .stream()
                        .map(f -> CourtSmartOggDetails.fromCourtSmartFile(allOggPaths.get(f.getFilename()), f))
                        .collect(Collectors.toList()))
                .build();
    }

    private static List<CourtSmartEventDetails> fromCourtSmartTags(List<Tag> tags) {
        return tags.stream()
                .map(CourtSmartEventDetails::fromCourtSmartTag)
                .collect(Collectors.toList());
    }
}
