package transcribe;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.inject.Inject;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.transcribe.TranscribeClient;
import software.amazon.awssdk.services.transcribe.model.GetTranscriptionJobRequest;
import software.amazon.awssdk.services.transcribe.model.GetTranscriptionJobResponse;
import software.amazon.awssdk.services.transcribe.model.LanguageCode;
import software.amazon.awssdk.services.transcribe.model.Media;
import software.amazon.awssdk.services.transcribe.model.MediaFormat;
import software.amazon.awssdk.services.transcribe.model.StartTranscriptionJobRequest;
import software.amazon.awssdk.services.transcribe.model.StartTranscriptionJobResponse;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJob;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJobStatus;

@Slf4j
@Value
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TranscriptionProcessor {

    private final TranscribeClient transcribe;

    private TranscriptionJob transcribe(String s3Uri) {
        UUID uuid = UUID.randomUUID();
        StartTranscriptionJobRequest request = StartTranscriptionJobRequest.builder()
                .transcriptionJobName(uuid.toString())
                .media(Media.builder().mediaFileUri(s3Uri).build())
                .mediaFormat(MediaFormat.MP3)
                .languageCode(LanguageCode.EN_US)
                .build();
        log.info("Will start transcription job for file {}", s3Uri);
        StartTranscriptionJobResponse response = transcribe.startTranscriptionJob(request);
        log.info("Did start transcription job {}", response.transcriptionJob().transcriptionJobName());
        return response.transcriptionJob();
    }

    @SneakyThrows
    private void getTranscriptionJobUntilTerminus(String transcriptionJobName) {
        TranscriptionJobStatus status;
        do {
            GetTranscriptionJobRequest request = GetTranscriptionJobRequest.builder()
                    .transcriptionJobName(transcriptionJobName)
                    .build();
            log.info("Will get transcription job {}", transcriptionJobName);
            GetTranscriptionJobResponse response = transcribe.getTranscriptionJob(request);
            log.info("Did get transcription job {}", response.transcriptionJob());
            status = response.transcriptionJob().transcriptionJobStatus();
            Thread.sleep(2000);
        } while (!TranscriptionJobStatus.COMPLETED.equals(status) &&
                !TranscriptionJobStatus.FAILED.equals(status));
    }

    public void process(List<String> s3Uris) {
        log.info("Will start transcription jobs for files {}", s3Uris);
        List<TranscriptionJob> jobs = s3Uris.stream()
                .map(this::transcribe)
                .collect(Collectors.toList());
        log.info("Did start transcription jobs {}", jobs.stream()
                .map(TranscriptionJob::transcriptionJobName)
                .collect(Collectors.toList()));
        jobs.stream().map(TranscriptionJob::transcriptionJobName)
                .forEach(this::getTranscriptionJobUntilTerminus);
    }

}
