package kr.mojuk.itoeic.test.wordpackTest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wordpack")
public class WordpackTestController {

	private final WordpackTestServise wordpackTestServise;

    public WordpackTestController(WordpackTestServise wordpackTestServise) {
        this.wordpackTestServise = wordpackTestServise;
    }
    
    @PostMapping("/test")
    public ResponseEntity<Void> wordpackTestResultProcess(@RequestBody WordpackTestDTO.Request request) {
    	wordpackTestServise.processTestResult(request);
        return ResponseEntity.ok().build();
    }
}
