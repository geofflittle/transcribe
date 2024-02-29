package transcribe.services;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import transcribe.model.CourtSmartOggDetails;
import transcribe.model.CourtSmartSessionDetails;
import transcribe.model.CourtSmartTranscribeRequest;
import transcribe.model.CourtSmartTranscribeResponse;

@Slf4j
@Value
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CourtSmartZipTranscriber {

    @NonNull
    private final Unzipper unzipper;
    @NonNull
    private final CourtSmartDirParser parser;
    @NonNull
    private final FileRenamer renamer;
    @NonNull
    private final S3TranscriptionStarter starter;
    @NonNull
    private final S3TranscriptPoller poller;
    @NonNull
    private final S3TranscriptDownloader downloader;
    @NonNull
    private final TranscriptFormatter formatter;

    private CourtSmartTranscribeRequest createTranscribeRequest(Path outputDir, ZoneId fileZoneId, String sessionDetail,
            String caseName, CourtSmartOggDetails oggDetails) {
        return CourtSmartTranscribeRequest.builder()
                .audioFile(oggDetails.getPath())
                .recordingStart(LocalDateTime.ofInstant(oggDetails.getStartTime(), fileZoneId))
                .transcriptOutputDir(outputDir)
                .transcriptDocTitle(sessionDetail)
                .caseName(caseName)
                .build();
    }

    private List<CourtSmartTranscribeRequest> createTranscribeRequests(Path outputDir, ZoneId fileZoneId,
            CourtSmartSessionDetails session) {
        return session.getOggDetailss().stream()
                .map(o -> createTranscribeRequest(outputDir, fileZoneId, session.getDetail(), session.getCaseName(), o))
                .collect(Collectors.toList());
    }

    private List<CourtSmartTranscribeRequest> createTranscribeRequests(Path outputDir, ZoneId fileZoneId,
            List<CourtSmartSessionDetails> sessions) {
        return sessions.stream()
                .map(session -> createTranscribeRequests(outputDir, fileZoneId, session))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private CompletableFuture<Optional<CourtSmartTranscribeResponse>> processRequest(
            CourtSmartTranscribeRequest request) {
        var inputAudio = renamer.rename(request.getAudioFile(), request.getRecordingStart());
        return starter.start(inputAudio)
                .thenCompose(job -> poller.pollS3Object(job.transcriptionJobName()))
                .thenCompose(om -> downloader.download(om))
                .thenApply(t -> t.getResults().getTranscripts().get(0).getTranscript())
                .thenApply(t -> {
                    if (StringUtils.isBlank(t)) {
                        log.info("Transcript content is blank");
                        return Optional.empty();
                    }
                    formatter.format(request.getTranscriptOutputDir(), request.getTranscriptDocTitle(),
                            request.getCaseName(),
                            inputAudio.getFileName().toString(), t);
                    return Optional.of(CourtSmartTranscribeResponse.builder()
                            .build());
                });
    }

    public void transcribe(Path courtSmartZip, ZoneId fileZoneId, Path outputDir) {
        var courtSmartDir = unzipper.unzip(courtSmartZip, Path.of("/tmp"));
        var sessions = parser.parse(courtSmartDir);
        var transcribeRequests = createTranscribeRequests(outputDir, fileZoneId,
                sessions);
        var transcribeResponseFutures = transcribeRequests
                .parallelStream()
                .map(this::processRequest)
                .collect(Collectors.toList());
        CompletableFuture.allOf(transcribeResponseFutures.toArray(CompletableFuture[]::new)).join();
    }

}
