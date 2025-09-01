// 경로: kr/mojuk/itoeic/test/userWords/UserWordsController.java

package kr.mojuk.itoeic.test.userWords;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession; // HttpSession import
import kr.mojuk.itoeic.test.userWords.DTO.*;
import lombok.*;

@RestController
@RequestMapping("/api/user-words")
@RequiredArgsConstructor
public class UserWordsController {

    private final UserWordsService userWordService;

    /**
     * 사용자의 단어장 조회 API
     */
    @GetMapping
    public ResponseEntity<List<UserWordResponseDto>> getUserWords(HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        // 세션에 userId가 없으면 (로그인하지 않은 경우) 401 Unauthorized 응답
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }

        List<UserWordResponseDto> words = userWordService.getUserWords(userId);
        return ResponseEntity.ok(words);
    }

    /**
     * 사용자의 단어장에 단어 추가 API
     */
    @PostMapping
    public ResponseEntity<String> addWord(@RequestBody UserWordRequestDto requestDto, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        
        // 세션에 userId가 없으면 (로그인하지 않은 경우) 401 Unauthorized 응답
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        userWordService.addUserWord(userId, requestDto.getWordId());
        return ResponseEntity.ok("단어 추가 완료");
    }

    /**
     * 사용자의 단어장에서 여러 단어 삭제 API
     */
    @DeleteMapping
    public ResponseEntity<String> deleteWords(@RequestBody UserWordDeleteRequestDto requestDto, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        
        // 세션에 userId가 없으면 (로그인하지 않은 경우) 401 Unauthorized 응답
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        
        userWordService.deleteUserWords(userId, requestDto.getWordIds());
        return ResponseEntity.ok("선택된 단어 삭제 완료");
    }
}