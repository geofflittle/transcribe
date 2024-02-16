package transcribe.services;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJob;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJobStatus;
import transcribe.aws.facades.TranscribeFacade;
import transcribe.aws.model.S3ObjectMetadata;
import transcribe.config.AppModule;

@Slf4j
@Value
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class S3TranscriptPoller {

    @Named(AppModule.POLL_SLEEP_MILLIS_NAME)
    private final Long pollSleepMillis;

    private final TranscribeFacade transcribe;

    @SneakyThrows
    private void sneakySleep(long timeout) {
        log.info("Will sleep for {} millis", timeout);
        TimeUnit.MILLISECONDS.sleep(timeout);
        log.info("Did sleep for {} millis", timeout);
    }

    private CompletableFuture<TranscriptionJob> pollJob(String jobName) {
        return transcribe.getTranscriptionJob(jobName)
                .thenCompose(job -> {
                    TranscriptionJobStatus status = job.transcriptionJobStatus();
                    if (TranscriptionJobStatus.COMPLETED.equals(status) ||
                            TranscriptionJobStatus.FAILED.equals(status)) {
                        log.info("Transcription job {} completed with status {}", jobName, status);
                        return CompletableFuture.completedFuture(job);
                    }
                    log.info("Transcription job {} not completed with status {}", jobName, status);
                    sneakySleep(pollSleepMillis);
                    return pollJob(jobName);
                });
    }

    public CompletableFuture<S3ObjectMetadata> pollS3Object(String jobName) {
        return pollJob(jobName).thenApply(job -> S3ObjectMetadata.fromFileUri(job.transcript().transcriptFileUri()));
    }

}
