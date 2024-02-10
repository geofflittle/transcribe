package transcribe.services;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import transcribe.aws.model.S3ObjectMetadata;
import transcribe.config.AppModule;

@Slf4j
@Value
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TranscriptProcessor {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Named(AppModule.S3_ROOT_BUCKET_NAME_NAME)
    private final String bucket;
    private final S3FileUploader uploader;
    private final S3TranscriptManager transcriptionManager;
    private final S3TranscriptDownloader downloader;
    private final TranscriptFormatter formatter;

    public void process(String inputAudioFilename, String outputDir, String documentTitle, String caseName) {
        String jobName = String.format("%s_%s", LocalDate.now().format(DATE_FORMATTER), UUID.randomUUID().toString());
        File inputAudioFile = new File(inputAudioFilename);
        S3ObjectMetadata audioFileS3Meta = uploader.upload(jobName, inputAudioFile);
        S3ObjectMetadata transcriptFileS3Meta = transcriptionManager.transcribe(jobName, inputAudioFile,
                audioFileS3Meta);
        String transcript = downloader.download(transcriptFileS3Meta);
        formatter.format(inputAudioFile, outputDir, documentTitle, caseName, transcript);
    }
}
