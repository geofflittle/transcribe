package transcribe.commands;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import com.google.inject.Inject;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import transcribe.Command;
import transcribe.services.S3TranscriptFormatter;

@Slf4j
@Value
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class FormatS3TranscriptCommand implements Command {

    private static final String BUCKET_SHORT = "b";
    private static final String KEY_SHORT = "k";
    private static final String OUTPUT_SHORT = "o";
    private static final String TITLE_SHORT = "d";
    private static final String CASE_SHORT = "c";

    @NonNull
    private final Option commandOption = Option.builder("f")
            .argName("format")
            .desc("Format an existing transcription")
            .optionalArg(true)
            .build();

    @NonNull
    private final List<Option> options = List.of(
            Option.builder(BUCKET_SHORT)
                    .argName("s3Bucket")
                    .desc("S3 bucket that holds the transcript s3 object")
                    .optionalArg(true)
                    .hasArg()
                    .build(),
            Option.builder(KEY_SHORT)
                    .argName("s3Key")
                    .desc("S3 key that represents the transcript s3 object")
                    .optionalArg(true)
                    .hasArg()
                    .build(),
            Option.builder(OUTPUT_SHORT)
                    .argName("outputDir")
                    .desc("The output directory of the formatted transcript")
                    .optionalArg(true)
                    .hasArg()
                    .build(),
            Option.builder(TITLE_SHORT)
                    .argName("transcriptDocTitle")
                    .desc("The transcript's doc title")
                    .optionalArg(true)
                    .hasArg()
                    .build(),
            Option.builder(CASE_SHORT)
                    .argName("caseName")
                    .desc("The case from which the audio transcript is taken")
                    .optionalArg(true)
                    .hasArg()
                    .build());

    @NonNull
    private final S3TranscriptFormatter formatter;

    public void run(CommandLine cmd) {
        var s3Bucket = cmd.getOptionValue(BUCKET_SHORT);
        var s3Key = cmd.getOptionValue(KEY_SHORT);
        var outputDirStr = cmd.getOptionValue(OUTPUT_SHORT);
        var transcriptDocTitle = cmd.getOptionValue(TITLE_SHORT);
        var caseName = cmd.getOptionValue(CASE_SHORT);

        log.info("Will process with s3 bucket {}, s3 key {}, output dir {}, transcript doc title {}, and case name {}",
                s3Bucket, s3Key, outputDirStr, transcriptDocTitle, caseName);
        formatter.format(outputDirStr, transcriptDocTitle, caseName, s3Bucket, s3Key);
        log.info("Did process with s3 bucket {}, s3 key {}, output dir {}, transcript doc title {}, and case name {}",
                s3Bucket, s3Key, outputDirStr, transcriptDocTitle, caseName);
    }
}
