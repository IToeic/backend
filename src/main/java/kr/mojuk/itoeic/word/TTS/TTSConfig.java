package kr.mojuk.itoeic.word.TTS;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.polly.PollyClient;

@Configuration
public class TTSConfig {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Bean
    public PollyClient pollyClient() {
        // @Value로 주입받은 accessKey와 secretKey를 사용합니다.
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return PollyClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
