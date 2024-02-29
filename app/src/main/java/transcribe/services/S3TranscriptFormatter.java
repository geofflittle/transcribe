package transcribe.services;

import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import transcribe.aws.model.S3ObjectMetadata;
import transcribe.model.S3TranscriptFormatRequest;
import transcribe.model.S3TranscriptFormatResponse;

@Slf4j
@Value
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class S3TranscriptFormatter {

    @NonNull
    private final S3TranscriptDownloader downloader;
    @NonNull
    private final TranscriptFormatter formatter;

    private CompletableFuture<Optional<S3TranscriptFormatResponse>> processRequest(
            S3TranscriptFormatRequest request) {
        return downloader.download(request.getTranscriptS3ObjectMeta())
                .thenApply(t -> t.getResults().getTranscripts().get(0).getTranscript())
                .thenApply(t -> {
                    if (StringUtils.isBlank(t)) {
                        log.info("Transcript content is blank");
                        return Optional.empty();
                    }
                    formatter.format(request.getTranscriptOutputDir(), request.getTranscriptDocTitle(),
                            request.getCaseName(), request.getAudioFilename(), t);
                    return Optional.of(S3TranscriptFormatResponse.builder().build());
                });
    }

    public void format(String outputDirStr, String transcriptDocTitle, String caseName, String s3Bucket, String s3Key) {
        // We assume that the transcript s3 key contains the audio file name
        var audioFilename = s3Key.substring(s3Key.lastIndexOf("/") + 1).replace("-transcript.json", "");
        processRequest(S3TranscriptFormatRequest.builder()
                .transcriptS3ObjectMeta(S3ObjectMetadata.builder()
                        .bucket(s3Bucket)
                        .key(s3Key)
                        .build())
                .transcriptOutputDir(Path.of(outputDirStr))
                .transcriptDocTitle(transcriptDocTitle)
                .caseName(caseName)
                .audioFilename(audioFilename)
                .build()).join();
    }

}
