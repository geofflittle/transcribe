package transcribe.services;

import java.nio.file.Files;
import java.nio.file.Path;

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
        var fileName = path.getFileName().toString();
        var dotIndex = fileName.lastIndexOf('.');
        return fileName.substring(0, dotIndex);
    }

    @SneakyThrows
    public Path unzip(Path zipFileFile, Path parentDestDir) {
        var destDir = parentDestDir.resolve(getRawFilename(zipFileFile));
        try (var zipFile = new ZipFile(zipFileFile)) {
            var entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                var entry = entries.nextElement();
                log.info("Found entry {}", entry.toString());
                var file = destDir.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(file);
                    continue;
                }
                var parent = file.getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }
                try (var is = zipFile.getInputStream(entry);
                        var os = Files.newOutputStream(file)) {
                    IOUtils.copy(is, os);
                }
            }
        }
        return destDir;
    }
}
