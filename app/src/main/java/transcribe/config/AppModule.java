package transcribe.config;

import java.time.Duration;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.core.retry.backoff.BackoffStrategy;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.transcribe.TranscribeAsyncClient;
import transcribe.Command;
import transcribe.commands.FormatS3TranscriptCommand;
import transcribe.commands.TranscribeCourtSmartAudioCommand;

public class AppModule extends AbstractModule {

    public static final String POLL_SLEEP_MILLIS_NAME = "POLL_SLEEP_MILLIS_NAME";
    public static final Long POLL_SLEEP_MILLIS = 2000L;

    public static final String S3_ROOT_BUCKET_NAME_NAME = "S3_ROOT_BUCKET_NAME";
    private static final String S3_ROOT_BUCKET_NAME = "transcribe-legal-audio";

    public void configure() {
        bindConstant().annotatedWith(Names.named(POLL_SLEEP_MILLIS_NAME)).to(POLL_SLEEP_MILLIS);
        bindConstant().annotatedWith(Names.named(S3_ROOT_BUCKET_NAME_NAME)).to(S3_ROOT_BUCKET_NAME);
    }

    @Provides
    @Singleton
    public ClientOverrideConfiguration clientOverrideConfiguration() {
        return ClientOverrideConfiguration.builder()
                .retryPolicy(RetryPolicy.builder()
                        .numRetries(10)
                        .backoffStrategy(BackoffStrategy.defaultStrategy())
                        .build())
                .build();
    }

    @Provides
    @Singleton
    public SdkAsyncHttpClient sdkAsyncHttpClient() {
        return NettyNioAsyncHttpClient.builder()
                .connectionTimeout(Duration.ofMinutes(1))
                .connectionAcquisitionTimeout(Duration.ofMinutes(10))
                .maxConcurrency(100)
                .build();
    }

    @Provides
    @Singleton
    public S3AsyncClient s3Client(ClientOverrideConfiguration config, SdkAsyncHttpClient httpClient) {
        return S3AsyncClient.builder()
                .region(Region.US_EAST_2)
                .overrideConfiguration(config)
                .httpClient(httpClient)
                .build();
    }

    @Provides
    @Singleton
    public TranscribeAsyncClient transcribeClient(ClientOverrideConfiguration config, SdkAsyncHttpClient httpClient) {
        return TranscribeAsyncClient.builder()
                .region(Region.US_EAST_2)
                .overrideConfiguration(config)
                .httpClient(httpClient)
                .build();
    }

    @Provides
    @Singleton
    public Map<String, Command> commandMap(FormatS3TranscriptCommand formatS3Transcript,
            TranscribeCourtSmartAudioCommand transcribeCourtSmartAudio) {
        return Map.ofEntries(
                Map.entry(formatS3Transcript.getCommandOption().getOpt(), formatS3Transcript),
                Map.entry(transcribeCourtSmartAudio.getCommandOption().getOpt(), transcribeCourtSmartAudio));
    }

}
