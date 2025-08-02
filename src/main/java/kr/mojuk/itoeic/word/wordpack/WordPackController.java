package kr.mojuk.itoeic.word.wordpack;

import kr.mojuk.itoeic.word.progress.ProgressDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wordpacks")
public class WordPackController {

    private final WordPackService wordPackService;

    public WordPackController(WordPackService wordPackService) {
        this.wordPackService = wordPackService;
    }

    @GetMapping("/progress")
    public List<ProgressDTO> getProgress(@RequestParam("userId") String userId) {
        return wordPackService.getWordPackProgressList(userId);
    }
}
