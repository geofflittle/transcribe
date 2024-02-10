package transcribe;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.google.inject.Inject;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Slf4j
@Value
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class S3UriDownloader {

    private final S3Facade s3;

    @SneakyThrows
    public void download(String uriStr, String outputDir) {
        log.info("Will download {} to {}", uriStr, outputDir);
        URI uri = URI.create(uriStr);
        String path = uri.getPath().substring(1); // Remove the leading '/'
        int firstSlashIndex = path.indexOf('/');
        String bucket = path.substring(0, firstSlashIndex);
        String key = path.substring(firstSlashIndex + 1);
        String fileName = path.substring(path.lastIndexOf('/') + 1);

        Path outputPath = Path.of(outputDir + "/" + fileName);
        ResponseInputStream<GetObjectResponse> objStream = s3.getObject(bucket, key);
        Files.copy(objStream, outputPath, StandardCopyOption.REPLACE_EXISTING);
        log.info("Did download {} to {}", uriStr, outputDir);
    }

}
