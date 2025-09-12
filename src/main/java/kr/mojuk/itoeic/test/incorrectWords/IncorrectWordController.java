package kr.mojuk.itoeic.test.incorrectWords;

import jakarta.servlet.http.HttpSession; // HttpSession import 추가
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; // HttpStatus import 추가
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections; // Collections import 추가
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/incorrect-words")
public class IncorrectWordController {

    private final IncorrectWordService incorrectWordService;

    @GetMapping
    public ResponseEntity<List<IncorrectWordResponseDTO>> getIncorrectWords(HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        // 세션에 userId가 없으면 (로그인하지 않은 경우) 401 Unauthorized 응답
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }
        
        List<IncorrectWordResponseDTO> incorrectWords = incorrectWordService.getIncorrectWordsByUserId(userId);
        return ResponseEntity.ok(incorrectWords);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteIncorrectWords(
            @RequestBody IncorrectWordDeleteRequestDTO requestDTO,
            HttpSession session) {
        
        String userId = (String) session.getAttribute("userId");

        // 세션에 userId가 없으면 (로그인하지 않은 경우) 401 Unauthorized 응답
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        incorrectWordService.deleteIncorrectWords(userId, requestDTO.getWordIds());
        return ResponseEntity.ok("선택된 단어 삭제 완료");
    }
    
    @GetMapping("/pack/{packId}")
    public ResponseEntity<Map<String, Long>> getIncorrectWordsCountByPack(
            @PathVariable("packId") Integer packId,
            HttpSession session) {

        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            // 로그인하지 않은 경우 401 Unauthorized 응답
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 서비스의 count 메소드를 호출합니다.
        long count = incorrectWordService.countIncorrectWordsByUserIdAndPackId(userId, packId);
        
        // 응답을 {"count": 숫자} 형태의 JSON으로 만들어 반환합니다.
        Map<String, Long> response = Collections.singletonMap("count", count);
        
        return ResponseEntity.ok(response);
    }
}
