package transcribe;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.transcribe.TranscribeClient;

public class AppModule extends AbstractModule {

    private static final String S3_ROOT_BUCKET_NAME = "transcribe-audio-cli";
    public static final String S3_MEDIA_BUCKET_NAME = "S3_MEDIA_BUCKET_NAME";
    public static final String S3_TRANSCRIPT_BUCKET_NAME = "S3_OUTPUT_BUCKET_NAME";

    public void configure() {
        bindConstant().annotatedWith(Names.named(S3_MEDIA_BUCKET_NAME)).to(S3_ROOT_BUCKET_NAME + "/input");
        bindConstant().annotatedWith(Names.named(S3_TRANSCRIPT_BUCKET_NAME)).to(S3_ROOT_BUCKET_NAME + "/output");
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
