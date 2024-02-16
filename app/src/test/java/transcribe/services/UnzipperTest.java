package transcribe.services;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import lombok.extern.slf4j.Slf4j;
import transcribe.config.IntegrationModule;

@Slf4j
public class UnzipperTest {

    private static final String ARCHIVE_FILENAME = "OneDrive_2024-02-12.zip";
    private static Unzipper unzipper;

    @BeforeAll
    public static void setup() {
        Injector injector = Guice.createInjector(new IntegrationModule());
        unzipper = injector.getInstance(Unzipper.class);
    }

    @Test
    public void unzip() {
        unzipper.unzip(Path.of("./src/test/resources/", ARCHIVE_FILENAME), Path.of("/tmp"));
    }

}
