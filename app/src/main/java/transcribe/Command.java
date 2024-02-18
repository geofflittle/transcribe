package transcribe;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

public interface Command {
    public Option getCommandOption();

    public List<Option> getOptions();

    public void run(CommandLine cmd);
}
