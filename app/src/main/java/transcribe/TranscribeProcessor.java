package transcribe;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJob;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TranscribeProcessor {

    @Named(AppModule.S3_MEDIA_BUCKET_NAME)
    private final String s3MediaBucket;
    @Named(AppModule.S3_TRANSCRIPT_BUCKET_NAME)
    private final String s3TranscriptBucket;
    private final DirectoryUploader directoryUploader;
    private final TranscriptionManager transcriptionManager;
    private final Downloader downloader;

    public void process(String inputDir, String outputDir) {
        log.info("Will upload {} to {}", inputDir, s3MediaBucket);
        List<String> s3MediaUris = directoryUploader.upload(new File(inputDir), s3MediaBucket);
        log.info("Did upload {} to {}", inputDir, s3MediaBucket);
        log.info("Will transcribe {} to {}", s3MediaUris, s3TranscriptBucket);
        List<TranscriptionJob> jobs = s3MediaUris.stream()
                .map(uri -> transcriptionManager.transcribe(uri, s3TranscriptBucket))
                .collect(Collectors.toList());
        log.info("Did transcribe {} to {}", s3MediaUris, s3TranscriptBucket);
        jobs.stream().forEach(job -> {
            String fileUri = job.transcript().transcriptFileUri();
            downloader.download(fileUri);
        });
    }
}
