package transcribe;

import java.util.Arrays;

import com.google.inject.Guice;
import com.google.inject.Injector;

import lombok.extern.slf4j.Slf4j;
import transcribe.config.AppModule;
import transcribe.services.PrereqEnsurer;

// TODO: Get app to read logging.properties file
@Slf4j
public class App {

    public static void main(String[] args) {
        log.info("Will run with args {}", Arrays.toString(args));
        Injector injector = Guice.createInjector(new AppModule());
        PrereqEnsurer ensurer = injector.getInstance(PrereqEnsurer.class);
        ensurer.ensure();

        CommandRunner runner = injector.getInstance(CommandRunner.class);
        runner.run(args);
        log.info("Did run with args {}", Arrays.toString(args));
    }
}
