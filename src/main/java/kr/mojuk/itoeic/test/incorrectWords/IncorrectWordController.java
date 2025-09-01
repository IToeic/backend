package kr.mojuk.itoeic.test.incorrectWords;

import jakarta.servlet.http.HttpSession; // HttpSession import 추가
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; // HttpStatus import 추가
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections; // Collections import 추가
import java.util.List;

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
}
