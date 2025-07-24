package kr.mojuk.itoeic.word.wordpack;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wordpacks")
@RequiredArgsConstructor
public class WordPackController {

    private final WordPackService wordPackService;

    // ✅ GET /api/wordpacks → 모든 단어팩 조회
    @GetMapping
    public List<WordPackDTO.Response> getAllWordPacks() {
        return wordPackService.getAllWordPacks();
    }
}
