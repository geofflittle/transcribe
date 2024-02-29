package transcribe;

import java.util.Arrays;

import com.google.inject.Guice;

import lombok.extern.slf4j.Slf4j;
import transcribe.config.AppModule;
import transcribe.services.PrereqEnsurer;

@Slf4j
public class App {

    public static void main(String[] args) {
        log.info("Will run with args {}", Arrays.toString(args));
        var injector = Guice.createInjector(new AppModule());
        var ensurer = injector.getInstance(PrereqEnsurer.class);
        ensurer.ensure();

        var runner = injector.getInstance(CommandRunner.class);
        runner.run(args);
        log.info("Did run with args {}", Arrays.toString(args));
    }
}
