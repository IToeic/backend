package kr.mojuk.itoeic.test.dailyTest;

import kr.mojuk.itoeic.test.dailyTest.WordIdListDTO;
import kr.mojuk.itoeic.test.testDTO.TestResultRequest;

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

    // 단어 세트를 PENDING으로 저장하는 API
    @PostMapping("/start-set")
    public ResponseEntity<Void> startTestSet(
            @RequestBody WordIdListDTO request,
            HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        dailyTestServise.createPendingWords(userId, request.getWordIds());
        return ResponseEntity.ok().build();
    }
    
    // 단어 세트를 COMPLETE로 수정하는 API
    @PostMapping("/daily")
    public ResponseEntity<Void> dailyTestResultProcess(
    		@RequestBody TestResultRequest request,
    		HttpSession session) {
    	String userId = (String) session.getAttribute("userId");
    	dailyTestServise.processTestResult(userId, request);
        return ResponseEntity.ok().build();
    }
    
    //단어 세트를 LEARNING으로 수정하는 API
    @PostMapping("/learning-word")
    public ResponseEntity<Void> startWordLearning(
            @RequestBody StartLearningRequestDTO requestDTO,
            HttpSession session) {
        
        String userId = (String) session.getAttribute("userId");
        
        dailyTestServise.updateStatusToLearning(userId, requestDTO.getWordId());
        
        return ResponseEntity.ok().build();
    }
}