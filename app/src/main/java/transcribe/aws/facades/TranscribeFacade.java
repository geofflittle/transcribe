package transcribe.aws.facades;

import java.util.concurrent.CompletableFuture;

import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.transcribe.TranscribeAsyncClient;
import software.amazon.awssdk.services.transcribe.model.GetTranscriptionJobRequest;
import software.amazon.awssdk.services.transcribe.model.GetTranscriptionJobResponse;
import software.amazon.awssdk.services.transcribe.model.LanguageCode;
import software.amazon.awssdk.services.transcribe.model.Media;
import software.amazon.awssdk.services.transcribe.model.MediaFormat;
import software.amazon.awssdk.services.transcribe.model.Settings;
import software.amazon.awssdk.services.transcribe.model.StartTranscriptionJobRequest;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJob;
import transcribe.aws.model.S3ObjectMetadata;

@Slf4j
@Value
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TranscribeFacade {

    private final TranscribeAsyncClient transcribe;

    public CompletableFuture<TranscriptionJob> startTranscription(String jobName, S3ObjectMetadata inputObjectMeta,
            S3ObjectMetadata outpuObjectMeta) {
        var request = StartTranscriptionJobRequest.builder()
                .transcriptionJobName(jobName)
                .outputBucketName(outpuObjectMeta.getBucket())
                .outputKey(outpuObjectMeta.getKey())
                .media(Media.builder().mediaFileUri(inputObjectMeta.toS3Uri()).build())
                .mediaFormat(MediaFormat.MP3)
                .languageCode(LanguageCode.EN_US)
                .settings(Settings.builder()
                        .showAlternatives(false)
                        .build())
                .build();
        log.info("Will start transcription job for s3 file {}", inputObjectMeta.toS3Uri());
        return transcribe.startTranscriptionJob(request)
                .thenApply(res -> {
                    TranscriptionJob job = res.transcriptionJob();
                    log.info("Did start transcription job {}", job.transcriptionJobName());
                    return job;
                });
    }

    public CompletableFuture<TranscriptionJob> getTranscriptionJob(String jobName) {
        log.info("Will get transcription job {}", jobName);
        return CompletableFuture.completedFuture(GetTranscriptionJobRequest.builder()
                .transcriptionJobName(jobName)
                .build())
                .thenCompose(transcribe::getTranscriptionJob)
                .thenApply(GetTranscriptionJobResponse::transcriptionJob);
    }

}
