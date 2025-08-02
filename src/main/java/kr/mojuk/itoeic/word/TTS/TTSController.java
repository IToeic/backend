package kr.mojuk.itoeic.word.TTS;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tts")
public class TTSController {

    private final TTSService ttsService;

    public TTSController(TTSService ttsService) {
        this.ttsService = ttsService;
    }

    @GetMapping("/{word}")
    public ResponseEntity<byte[]> getTTS(@PathVariable("word") String word) {
        byte[] audioData = ttsService.synthesizeSpeech(word);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Content-Disposition", "inline; filename=\"" + word + ".mp3\"");

        return new ResponseEntity<>(audioData, headers, HttpStatus.OK);
    }

}
