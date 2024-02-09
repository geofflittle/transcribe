package transcribe;

import com.google.inject.Inject;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Value
@Builder
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Downloader {

    public void download(String uri) {
        log.info("Will download {}", uri);
        // String outputFileName = "output.txt"; // Replace with your desired file name

        // try {
        // // Create URL object
        // URL url = new URL(urlString);

        // // Open a stream to the URL
        // try (BufferedReader reader = new BufferedReader(new
        // InputStreamReader(url.openStream()));
        // BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {

        // String line;

        // // Read from the URL and write to the file
        // while ((line = reader.readLine()) != null) {
        // writer.write(line);
        // writer.newLine();
        // }

        // System.out.println("Download completed. File saved as " + outputFileName);
        // }

        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

}
