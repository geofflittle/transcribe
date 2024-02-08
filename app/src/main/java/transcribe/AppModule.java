package transcribe;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.s3.S3Client;

public class AppModule extends AbstractModule {

    public static final String S3_BUCKET_NAME_NAME = "S3_BUCKET_NAME";

    public void configure() {

    }

    @Named(S3_BUCKET_NAME_NAME)
    @Provides
    public String s3BucketName() {
        return "transcribe-audio-cli";
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

}
