package kr.mojuk.itoeic.word.TTS;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.OutputFormat;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechRequest;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechResponse;
import software.amazon.awssdk.services.polly.model.VoiceId;
import software.amazon.awssdk.core.ResponseInputStream;

import java.io.IOException;

@Service
public class TTSService {

    private final PollyClient pollyClient;

    public TTSService(PollyClient pollyClient) {
        this.pollyClient = pollyClient;
    }

    public byte[] synthesizeSpeech(String text) {
        SynthesizeSpeechRequest request = SynthesizeSpeechRequest.builder()
                .text(text)
                .voiceId(VoiceId.JOANNA) // 고정된 목소리
                .outputFormat(OutputFormat.MP3)
                .build();

        try (ResponseInputStream<SynthesizeSpeechResponse> response = pollyClient.synthesizeSpeech(request)) {
            return response.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("TTS 변환 실패: " + text, e);
        }
    }
}
