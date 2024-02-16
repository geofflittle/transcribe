package transcribe.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import transcribe.config.IntegrationModule;

public class CourtSmartZipHandlerTest {

    private static final String ARCHIVE_FILENAME = "OneDrive_2024-02-12.zip";
    private static CourtSmartDirParser parser;

    @BeforeAll
    public static void setup() {
        Injector injector = Guice.createInjector(new IntegrationModule());
        parser = injector.getInstance(CourtSmartDirParser.class);
    }

    @Test
    public void test() {
        // parser.process(new File("./src/test/resources/", ARCHIVE_FILENAME),
        // ZoneId.of("America/New_York"));
    }
}
