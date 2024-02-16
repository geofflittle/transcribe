package transcribe.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileRenamer {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");

    @SneakyThrows
    public Path rename(Path oldFile, LocalDateTime newFilePrefix) {
        String formattedInstant = DATE_TIME_FORMAT.format(newFilePrefix);
        String newFilename = String.format("%s_%s", formattedInstant,
                oldFile.getFileName());
        Path newFile = oldFile.resolveSibling(newFilename);
        log.info("Will replace old file {} with new file {}", oldFile, newFile);
        Files.move(oldFile, newFile, StandardCopyOption.REPLACE_EXISTING);
        log.info("Did replace old file {} with new file {}", oldFile, newFile);
        return newFile;
    }

}
