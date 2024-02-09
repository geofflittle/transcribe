package transcribe;

import java.util.UUID;

import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJob;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJobStatus;

@Slf4j
@Value
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TranscriptionManager {

    private static final Long SLEEP_MILLIS = 2000L;

    private final TranscribeFacade transcribe;

    @SneakyThrows
    public TranscriptionJob transcribe(String s3MediaUri, String s3OutputBucket) {
        log.info("Will transcribe {}", s3MediaUri);
        String jobName = UUID.randomUUID().toString();
        TranscriptionJob job = transcribe.startTranscription(jobName, s3MediaUri, s3OutputBucket);
        do {
            job = transcribe.getTranscriptionJob(jobName);
            Thread.sleep(SLEEP_MILLIS);
        } while (!TranscriptionJobStatus.COMPLETED.equals(job.transcriptionJobStatus()) &&
                !TranscriptionJobStatus.FAILED.equals(job.transcriptionJobStatus()));
        log.info("Did transcribe {}", s3MediaUri);
        return job;
    }

}
