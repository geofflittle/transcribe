package transcribe.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

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

    @SneakyThrows
    private Pair<Store, Map<String, Path>> getStoreAndOggs(Path courtSmartDir) {
        try (Stream<Path> paths = Files.walk(courtSmartDir)) {
            MutablePair<Store, Map<String, Path>> result = paths.reduce(
                    new MutablePair<>(null, new HashMap<>()),
                    (acc, path) -> {
                        if (path.toString().endsWith(".csx")) {
                            Store store = parseCourtSmartSessions(path);
                            acc.setLeft(store);
                        } else if (path.toString().endsWith(".ogg")) {
                            acc.getRight().put(path.getFileName().toString(), path);
                        }
                        return acc;
                    },
                    (acc1, acc2) -> {
                        if (acc2.getLeft() != null) {
                            acc1.setLeft(acc2.getLeft());
                        }
                        acc1.getRight().putAll(acc2.getRight());
                        return acc1;
                    });
            return Pair.of(result.getLeft(), result.getRight());
        }
    }

    public List<CourtSmartSessionDetails> parse(Path courtSmartDir) {
        log.info("Will get store and oggs for dir {}", courtSmartDir);
        Pair<Store, Map<String, Path>> storeAndOggs = getStoreAndOggs(courtSmartDir);
        return storeAndOggs.getLeft().getSessions().stream()
                .map(s -> CourtSmartSessionDetails.fromCourtSmartSession(storeAndOggs.getRight(), s))
                .collect(Collectors.toList());
    }

}
