package kr.mojuk.itoeic.test.dailyTest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/api/words/test")
public class DailyTestController {

    private final DailyTestServise dailyTestServise;

    public DailyTestController(DailyTestServise dailyTestServise) {
        this.dailyTestServise = dailyTestServise;
    }

    @PostMapping("/daily")
    public ResponseEntity<Void> dailyTestResultProcess(
    		@RequestBody DailyTestDTO.Request request,
    		HttpSession session) {
    	String userId = (String) session.getAttribute("userId");
    	dailyTestServise.processTestResult(userId, request);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/learning-word")
    public ResponseEntity<Void> startWordLearning(
            @RequestBody StartLearningRequestDTO requestDTO,
            HttpSession session) {
        
        String userId = (String) session.getAttribute("userId");
        dailyTestServise.setTestLearning(userId, requestDTO.getWordId());
        
        return ResponseEntity.ok().build();
    }
}