package transcribe.model;

import java.time.Instant;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import transcribe.courtsmart.model.Tag;

@Value
@Builder
public class CourtSmartEventDetails {

    @NonNull
    private final Instant indexInstant;
    @Nullable
    private final String content;

    public static CourtSmartEventDetails fromCourtSmartTag(Tag tag) {
        return CourtSmartEventDetails.builder()
                .indexInstant(Instant.ofEpochSecond(tag.getIndexTime()))
                .content(StringUtils.isBlank(tag.getContent()) ? null : tag.getContent().trim())
                .build();
    }
}
