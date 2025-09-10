package kr.mojuk.itoeic.test.wordpackTest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/wordpack")
public class WordpackTestController {

	private final WordpackTestServise wordpackTestServise;

    public WordpackTestController(WordpackTestServise wordpackTestServise) {
        this.wordpackTestServise = wordpackTestServise;
    }
    
    @PostMapping("/test")
    public ResponseEntity<Void> wordpackTestResultProcess(@RequestBody WordpackTestDTO.Request request, HttpSession session) {
        // 세션에서 사용자 ID를 가져옵니다.
        String userId = (String) session.getAttribute("userId");
        
        // userId가 null일 경우의 예외 처리를 추가할 수 있습니다.
         if (userId == null) { 
        	 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); 
         }

        wordpackTestServise.processTestResult(userId, request);
        return ResponseEntity.ok().build();
    }
}
