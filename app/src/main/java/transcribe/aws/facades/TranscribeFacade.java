package transcribe.aws.facades;

import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.transcribe.TranscribeClient;
import software.amazon.awssdk.services.transcribe.model.GetTranscriptionJobRequest;
import software.amazon.awssdk.services.transcribe.model.GetTranscriptionJobResponse;
import software.amazon.awssdk.services.transcribe.model.LanguageCode;
import software.amazon.awssdk.services.transcribe.model.Media;
import software.amazon.awssdk.services.transcribe.model.MediaFormat;
import software.amazon.awssdk.services.transcribe.model.Settings;
import software.amazon.awssdk.services.transcribe.model.StartTranscriptionJobRequest;
import software.amazon.awssdk.services.transcribe.model.StartTranscriptionJobResponse;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJob;
import transcribe.aws.model.S3ObjectMetadata;

@Slf4j
@Value
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TranscribeFacade {

    private final TranscribeClient transcribe;

    public TranscriptionJob startTranscription(String jobName, String outputBucketName,
            String outputKey, S3ObjectMetadata objectMetadata) {
        StartTranscriptionJobRequest request = StartTranscriptionJobRequest.builder()
                .transcriptionJobName(jobName)
                .outputBucketName(outputBucketName)
                .outputKey(outputKey)
                .media(Media.builder().mediaFileUri(objectMetadata.toS3URIString()).build())
                .mediaFormat(MediaFormat.MP3)
                .languageCode(LanguageCode.EN_US)
                .settings(Settings.builder()
                        .showAlternatives(false)
                        .build())
                .build();
        log.info("Will start transcription job for file {}", objectMetadata.toS3URIString());
        StartTranscriptionJobResponse response = transcribe.startTranscriptionJob(request);
        log.info("Did start transcription job {}", response.transcriptionJob().transcriptionJobName());
        return response.transcriptionJob();
    }

    public TranscriptionJob getTranscriptionJob(String jobName) {
        GetTranscriptionJobRequest request = GetTranscriptionJobRequest.builder()
                .transcriptionJobName(jobName)
                .build();
        log.info("Will get transcription job {}", jobName);
        GetTranscriptionJobResponse response = transcribe.getTranscriptionJob(request);
        log.info("Did get transcription job {}", response.transcriptionJob());
        return response.transcriptionJob();
    }

}
