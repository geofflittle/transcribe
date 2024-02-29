package transcribe.services;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJob;
import transcribe.aws.facades.S3Facade;
import transcribe.aws.facades.TranscribeFacade;
import transcribe.aws.model.S3ObjectMetadata;
import transcribe.config.AppModule;

@Slf4j
@Value
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class S3TranscriptionStarter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Named(AppModule.S3_ROOT_BUCKET_NAME_NAME)
    private final String bucket;
    private final S3Facade s3;
    private final TranscribeFacade transcribe;
    private final S3TranscriptPoller transcriptionManager;

    /**
     * Generates a job name from the current date and a random guid and the audio
     * and transcript S3 keys, then uploads the provided file and starts its
     * transcription job.
     * 
     * @param inputAudio
     * @return
     */
    public CompletableFuture<TranscriptionJob> start(Path inputAudio) {
        log.info("Will start transcript of {}", inputAudio);
        var jobName = String.format("%s_%s", LocalDate.now().format(DATE_FORMATTER), UUID.randomUUID().toString());
        var audioS3Key = String.format("%s/%s", jobName, inputAudio.getFileName());
        var transcriptS3Key = audioS3Key + "-transcript.json";
        var transcriptS3Meta = S3ObjectMetadata.builder()
                .bucket(bucket)
                .key(transcriptS3Key)
                .build();
        return s3.putObject(bucket, audioS3Key, inputAudio)
                .thenCompose(om -> {
                    log.info("Will start transcription from {}/{} to {}", bucket, audioS3Key,
                            transcriptS3Meta.toS3Uri());
                    return transcribe.startTranscription(jobName, om, transcriptS3Meta);
                })
                .thenApply(j -> {
                    log.info("Will start transcription job {} from {}/{} to {}", j.transcriptionJobName(), bucket,
                            audioS3Key, transcriptS3Meta.toS3Uri());
                    return j;
                });

    }
}
