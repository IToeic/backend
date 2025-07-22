package kr.mojuk.itoeic.test.incorrectWord;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incorrect-words")
public class IncorrectWordController {

    private final IncorrectWordService incorrectWordService;

    public IncorrectWordController(IncorrectWordService incorrectWordService) {
        this.incorrectWordService = incorrectWordService;
    }

    @PostMapping
    public ResponseEntity<Void> addIncorrectWord(@RequestBody IncorrectWordDTO.Request request) {
        incorrectWordService.addIncorrectWord(request);
        return ResponseEntity.ok().build();
    }
}

