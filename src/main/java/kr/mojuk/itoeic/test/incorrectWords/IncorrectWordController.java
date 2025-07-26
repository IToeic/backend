package kr.mojuk.itoeic.test.incorrectWords;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/incorrect-words")
public class IncorrectWordController {

    private final IncorrectWordService incorrectWordService;

    // 사용자 ID로 틀린 단어 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<IncorrectWordResponseDTO>> getIncorrectWordsByUserId(@PathVariable("userId") String userId) {
        List<IncorrectWordResponseDTO> incorrectWords = incorrectWordService.getIncorrectWordsByUserId(userId);
        return ResponseEntity.ok(incorrectWords);
    }

    // 틀린 단어 삭제
    @DeleteMapping("/{incorrectWordId}")
    public ResponseEntity<String> deleteIncorrectWord(@PathVariable Integer incorrectWordId) {
        incorrectWordService.deleteIncorrectWord(incorrectWordId);
        return ResponseEntity.ok("삭제 완료");
    }
}
