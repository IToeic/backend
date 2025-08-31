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

    /**
     * GET /api/words/daily?wordpackId=N
     * 단어팩 N번에서 랜덤 5개 단어만 반환합니다.
     */
    @GetMapping("/daily")
    public ResponseEntity<List<WordDTO.Response>> getRandomWordsByPack(
            @RequestParam("wordpackId") Integer wordpackId,
            @RequestParam("userId") String userId) { // 🔥 userId 파라미터 추가
        return ResponseEntity.ok(wordService.getRandomWordsByPack(wordpackId, userId)); // 🔥 userId 전달
    }

    /**
     * GET /api/words/all?wordpackId=N
     * 단어팩 N번의 모든 단어를 반환합니다.
     */
    @GetMapping("/all")
    public ResponseEntity<List<WordDTO.Response>> getAllWordsByPack(
            @RequestParam("wordpackId") Integer wordpackId) {
        return ResponseEntity.ok(wordService.getAllWordsByPack(wordpackId));
    }

    /**
     * GET /api/words?wordpackIds=N,M,...
     * 여러 단어팩(N, M번)의 모든 단어를 반환합니다.
     * @RequestParam을 사용하면 쉼표로 구분된 id들을 List<Integer>로 자동 변환해 줍니다.
     */
    @GetMapping
    public ResponseEntity<List<WordDTO.Response>> getWordsByWordPackIds(
            @RequestParam("wordpackIds") List<Integer> wordpackIds) {
        return ResponseEntity.ok(wordService.getAllWordsByPackIds(wordpackIds));
    }
}
