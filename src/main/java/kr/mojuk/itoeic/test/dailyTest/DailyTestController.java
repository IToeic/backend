package kr.mojuk.itoeic.test.dailyTest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/words/test")
public class DailyTestController {

    private final DailyTestServise dailyTestServise;

    public DailyTestController(DailyTestServise dailyTestServise) {
        this.dailyTestServise = dailyTestServise;
    }

    @PostMapping("/daily")
    public ResponseEntity<Void> dailyTestResultProcess(@RequestBody DailyTestDTO.Request request) {
    	dailyTestServise.processTestResult(request);
        return ResponseEntity.ok().build();
    }
}