package transcribe;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.inject.Inject;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJob;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJobStatus;

@Slf4j
@Value
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TranscriptionManager {

    private static final Long SLEEP_MILLIS = 2000L;

    private final TranscribeFacade transcribe;

    @SneakyThrows
    public TranscriptionJob transcribe(String mediaFileUri, String s3OutputBucket, List<String> outputPrefixSegments) {
        String jobName = UUID.randomUUID().toString();
        String[] fileUriParts = mediaFileUri.split("/");
        String fileName = fileUriParts[fileUriParts.length - 1];
        String outputKey = Stream.of(outputPrefixSegments.stream(), Stream.of(fileName))
                .flatMap(Function.identity())
                .collect(Collectors.joining("/")) + ".txt";
        log.info("Will transcribe {} to {}/{}", mediaFileUri, s3OutputBucket, outputKey);
        TranscriptionJob job = transcribe.startTranscription(jobName, s3OutputBucket, outputKey, mediaFileUri);
        do {
            job = transcribe.getTranscriptionJob(jobName);
            Thread.sleep(SLEEP_MILLIS);
        } while (!TranscriptionJobStatus.COMPLETED.equals(job.transcriptionJobStatus()) &&
                !TranscriptionJobStatus.FAILED.equals(job.transcriptionJobStatus()));
        log.info("Did transcribe {} to {}/{}", mediaFileUri, s3OutputBucket, outputKey);
        return job;
    }

}
