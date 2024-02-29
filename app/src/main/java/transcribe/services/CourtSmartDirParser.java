package transcribe.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.inject.Inject;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import transcribe.courtsmart.model.Store;
import transcribe.model.CourtSmartSessionDetails;

@Slf4j
@Value
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CourtSmartDirParser {

    @NonNull
    private final XmlMapper mapper;

    @SneakyThrows
    private Store parseCourtSmartSessions(Path sessionsXml) {
        return mapper.readValue(sessionsXml.toFile(), Store.class);
    }

    public record StoreAndOggs(Store store, Map<String, Path> oggs) {
    }

    @SneakyThrows
    private StoreAndOggs getStoreAndOggs(Path courtSmartDir) {
        try (var paths = Files.walk(courtSmartDir)) {
            var result = paths.reduce(
                    new StoreAndOggs(null, new HashMap<>()),
                    (acc, path) -> {
                        if (path.toString().endsWith(".csx")) {
                            var store = parseCourtSmartSessions(path);
                            return new StoreAndOggs(store, acc.oggs());
                        }
                        if (path.toString().endsWith(".ogg")) {
                            acc.oggs().put(path.getFileName().toString(), path);
                        }
                        return acc;
                    },
                    (acc1, acc2) -> acc1);
            return new StoreAndOggs(result.store(), result.oggs());
        }
    }

    public List<CourtSmartSessionDetails> parse(Path courtSmartDir) {
        log.info("Will get store and oggs for dir {}", courtSmartDir);
        var storeAndOggs = getStoreAndOggs(courtSmartDir);
        return storeAndOggs.store().getSessions().stream()
                .map(s -> CourtSmartSessionDetails.fromCourtSmartSession(storeAndOggs.oggs(), s))
                .collect(Collectors.toList());
    }

}
