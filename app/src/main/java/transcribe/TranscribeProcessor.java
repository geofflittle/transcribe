package transcribe;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJob;

@Slf4j
@Value
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TranscribeProcessor {

    @Named(AppModule.S3_ROOT_BUCKET_NAME_NAME)
    private final String s3RootBucket;
    @Named(AppModule.S3_MEDIA_PREFIX_SEGMENTS_NAME)
    private final List<String> s3MediaPathSegments;
    @Named(AppModule.S3_TRANSCRIPT_PREFIX_SEGMENTS_NAME)
    private final List<String> s3TranscriptPathSegments;
    private final DirectoryUploader directoryUploader;
    private final TranscriptionManager transcriptionManager;
    private final S3UriDownloader downloader;

    public void process(String inputDir, String outputDir) {
        String s3MediaKeyPrefix = s3MediaPathSegments.stream().collect(Collectors.joining("/"));
        log.info("Will upload {} to {}/{}", inputDir, s3RootBucket, s3MediaKeyPrefix);
        List<String> s3MediaUris = directoryUploader.upload(s3RootBucket, s3MediaKeyPrefix, new File(inputDir));
        log.info("Did upload {} to {}/{}", inputDir, s3RootBucket, s3MediaKeyPrefix);

        log.info("Will transcribe {} to {}", s3MediaUris, s3RootBucket);
        List<TranscriptionJob> jobs = s3MediaUris.stream()
                .map(uri -> transcriptionManager.transcribe(uri, s3RootBucket, s3TranscriptPathSegments))
                .collect(Collectors.toList());
        log.info("Did transcribe {} to {}", s3MediaUris, s3RootBucket);
        jobs.stream().forEach(job -> {
            String fileUri = job.transcript().transcriptFileUri();
            downloader.download(fileUri, outputDir);
        });
    }
}
