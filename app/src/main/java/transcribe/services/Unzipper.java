package transcribe.services;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;

import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Value
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Unzipper {

    private String getRawFilename(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        return fileName.substring(0, dotIndex);
    }

    @SneakyThrows
    public Path unzip(Path zipFileFile, Path parentDestDir) {
        Path destDir = parentDestDir.resolve(getRawFilename(zipFileFile));
        try (ZipFile zipFile = new ZipFile(zipFileFile)) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                log.info("Found entry {}", entry.toString());
                Path file = destDir.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(file);
                    continue;
                }
                Path parent = file.getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }
                try (InputStream is = zipFile.getInputStream(entry);
                        OutputStream os = Files.newOutputStream(file)) {
                    IOUtils.copy(is, os);
                }
            }
        }
        return destDir;
    }
}
