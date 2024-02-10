package transcribe.config;

import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.transcribe.TranscribeClient;

public class AppModule extends AbstractModule {

    public static final String S3_ROOT_BUCKET_NAME_NAME = "S3_ROOT_BUCKET_NAME";
    private static final String S3_ROOT_BUCKET_NAME = "transcribe-audio-cli";

    public static final String S3_MEDIA_PREFIX_SEGMENTS_NAME = "S3_MEDIA_PREFIX_SEGMENTS_NAME";
    private static final List<String> S3_MEDIA_PREFIX_SEGMENTS = List.of("input");

    public static final String S3_TRANSCRIPT_PREFIX_SEGMENTS_NAME = "S3_TRANSCRIPT_PREFIX_SEGMENTS_NAME";
    private static final List<String> S3_TRANSCRIPT_PREFIX_SEGMENTS = List.of("output");

    public void configure() {
        bindConstant().annotatedWith(Names.named(S3_ROOT_BUCKET_NAME_NAME)).to(S3_ROOT_BUCKET_NAME);
    }

    @Provides
    @Named(S3_MEDIA_PREFIX_SEGMENTS_NAME)
    public List<String> s3MediaPrefixSegments() {
        return S3_MEDIA_PREFIX_SEGMENTS;
    }

    @Provides
    @Named(S3_TRANSCRIPT_PREFIX_SEGMENTS_NAME)
    public List<String> s3TranscriptPrefixSegments() {
        return S3_TRANSCRIPT_PREFIX_SEGMENTS;
    }

    @Provides
    @Singleton
    public SdkHttpClient sdkHttpClient() {
        return ApacheHttpClient.builder().build();
    }

    @Provides
    @Singleton
    public S3Client s3Client(SdkHttpClient httpClient) {
        return S3Client.builder()
                .httpClient(httpClient)
                .build();
    }

    @Provides
    @Singleton
    public TranscribeClient transcribeClient(SdkHttpClient httpClient) {
        return TranscribeClient.builder()
                .httpClient(httpClient)
                .build();
    }

}
