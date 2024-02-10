package transcribe.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import transcribe.aws.facades.S3Facade;
import transcribe.aws.model.S3ObjectMetadata;
import transcribe.aws.model.transcribe.Transcript;

@Slf4j
@Value
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class S3TranscriptDownloader {

    private final S3Facade s3;
    private final ObjectMapper mapper;

    @SneakyThrows
    public String download(S3ObjectMetadata s3ObjectMetadata) {
        ResponseInputStream<GetObjectResponse> objStream = s3.getObject(s3ObjectMetadata.getBucket(),
                s3ObjectMetadata.getKey());
        Transcript transcript = mapper.readValue(objStream, Transcript.class);
        return transcript.getResults().getTranscripts().get(0).getTranscript();
    }

}
