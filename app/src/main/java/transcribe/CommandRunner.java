package transcribe;

import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

import com.google.inject.Inject;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Value
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CommandRunner {

    @NonNull
    private final Map<String, Command> commands;

    @SneakyThrows
    private CommandLine getCommandLine(String[] args) {
        var options = new Options();
        var optionGroup = new OptionGroup();
        commands.values().forEach(c -> {
            optionGroup.addOption(c.getCommandOption());
            c.getOptions().forEach(options::addOption);
        });
        options.addOptionGroup(optionGroup);
        var parser = new DefaultParser();
        return parser.parse(options, args);
    }

    public void run(String[] args) {
        var cmd = getCommandLine(args);
        commands.entrySet().forEach(e -> {
            if (!cmd.hasOption(e.getKey())) {
                return;
            }
            e.getValue().run(cmd);
        });
    }

}
