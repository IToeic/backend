package kr.mojuk.itoeic.word.word;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;
    
     // GET /api/words/daily?wordpackId=N
     // 단어팩 N번에서 랜덤 5개 단어만 반환
     
    @GetMapping("/daily")
    public ResponseEntity<List<WordDTO.Response>> getDailyWords(
            @RequestParam("wordpackId") Integer wordpackId) {
        return ResponseEntity.ok(wordService.getRandomWordsByPack(wordpackId));
    }
}
