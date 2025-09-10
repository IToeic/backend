package kr.mojuk.itoeic.word.wordpack;

import kr.mojuk.itoeic.word.progress.ProgressDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/wordpacks")
public class WordPackController {

    private final WordPackService wordPackService;

    public WordPackController(WordPackService wordPackService) {
        this.wordPackService = wordPackService;
    }

    @GetMapping("/progress")
    public ResponseEntity<List<ProgressDTO>> getProgress(HttpSession session) { // 🔥 HttpSession 사용 및 반환 타입 변경
        String userId = (String) session.getAttribute("userId"); // 🔥 세션에서 userId 가져오기

        // 세션에 userId가 없으면 (로그인하지 않은 경우) 401 Unauthorized 응답
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }
        
        List<ProgressDTO> progressList = wordPackService.getWordPackProgressList(userId);
        return ResponseEntity.ok(progressList);
    }
}
