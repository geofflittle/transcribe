package transcribe.services;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import transcribe.aws.facades.S3Facade;
import transcribe.aws.model.S3ObjectMetadata;
import transcribe.aws.model.transcribe.Transcript;

@Slf4j
@Value
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class S3TranscriptDownloader {

    @NonNull
    private final S3Facade s3;
    @NonNull
    private final ObjectMapper mapper;

    @SneakyThrows
    private Transcript readTranscript(InputStream src) {
        return mapper.readValue(src, Transcript.class);
    }

    public CompletableFuture<Transcript> download(S3ObjectMetadata s3ObjectMetadata) {
        return s3.getObject(s3ObjectMetadata.getBucket(), s3ObjectMetadata.getKey())
                .thenApply(this::readTranscript);
    }

}
