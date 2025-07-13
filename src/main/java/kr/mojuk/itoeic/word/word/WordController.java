package kr.mojuk.itoeic.word.word;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/words")
public class WordController {

    private final WordService wordService;

    // ✅ 생성자 수동 작성 (Lombok 없이)
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    // ✅ GET /api/words/random
    @GetMapping("/daily")
    public ResponseEntity<List<WordDTO.Response>> getRandomWords() {
        List<WordDTO.Response> words = wordService.getRandomWords();
        return ResponseEntity.ok(words);
    }
}
