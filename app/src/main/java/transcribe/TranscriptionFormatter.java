package transcribe;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import transcribe.model.AwsTranscript;

@Slf4j
@Value
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TranscriptionFormatter {

    private final ObjectMapper mapper;

    @SneakyThrows
    private void render(String content) {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));
        document.open();
        document.add(new Paragraph(content));
        document.close();
        writer.close();
    }

    @SneakyThrows
    private AwsTranscript parse(File file) {
        return mapper.readValue(file, AwsTranscript.class);
    }

    public void format(String transcriptDirStr) {
        File transcriptDir = new File(transcriptDirStr);
        if (!transcriptDir.exists() || !transcriptDir.isDirectory()) {
            throw new RuntimeException("No directory " + transcriptDir);
        }
        String transcript = Arrays.stream(transcriptDir.listFiles())
                .map(f -> parse(f))
                .map(t -> t.getResults().getTranscripts().get(0).getTranscript())
                .collect(Collectors.joining("\n"));
        render(transcript);
        ;
    }

}
