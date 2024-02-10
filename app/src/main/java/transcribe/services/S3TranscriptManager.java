package transcribe.services;

import java.io.File;

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
public class S3TranscriptManager {

    private static final Long SLEEP_MILLIS = 2000L;

    @Named(AppModule.S3_ROOT_BUCKET_NAME_NAME)
    private final String bucket;

    private final TranscribeFacade transcribe;

    @SneakyThrows
    public S3ObjectMetadata transcribe(String jobName, File file, S3ObjectMetadata audioFileS3Meta) {
        String transcriptS3Key = String.format("%s/%s-transcript.json", jobName, file.getName());
        log.info("Will transcribe {} to {}/{}", audioFileS3Meta.toS3URIString(), bucket, transcriptS3Key);
        TranscriptionJob job = transcribe.startTranscription(jobName, bucket, transcriptS3Key, audioFileS3Meta);
        do {
            job = transcribe.getTranscriptionJob(jobName);
            Thread.sleep(SLEEP_MILLIS);
        } while (!TranscriptionJobStatus.COMPLETED.equals(job.transcriptionJobStatus()) &&
                !TranscriptionJobStatus.FAILED.equals(job.transcriptionJobStatus()));
        log.info("Did transcribe {} to {}/{}", audioFileS3Meta.toS3URIString(), bucket, transcriptS3Key);
        return S3ObjectMetadata.builder()
                .bucket(bucket)
                .key(transcriptS3Key)
                .build();
    }

}
