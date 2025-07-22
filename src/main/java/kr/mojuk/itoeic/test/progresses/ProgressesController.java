package kr.mojuk.itoeic.test.progresses;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progresses")
public class ProgressesController {

    private final ProgressesService progressesService;

    public ProgressesController(ProgressesService progressesService) {
        this.progressesService = progressesService;
    }

    @PostMapping("/complete")
    public ResponseEntity<Void> markCompleted(@RequestBody ProgressesDTO.Request request) {
        progressesService.markWordsAsCompleted(request);
        return ResponseEntity.ok().build();
    }
}
