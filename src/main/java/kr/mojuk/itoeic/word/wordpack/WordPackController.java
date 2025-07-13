package kr.mojuk.itoeic.word.wordpack;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wordpacks")
public class WordPackController {

    private final WordPackService wordPackService;

    // 💡 생성자 수동 작성 (Lombok 없이)
    public WordPackController(WordPackService wordPackService) {
        this.wordPackService = wordPackService;
    }

    // ✅ 단어팩 전체 조회
    @GetMapping
    public List<WordPackDTO.Response> getAllWordPacks() {
        return wordPackService.getAllWordPacks();
    }
}
