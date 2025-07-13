package kr.mojuk.itoeic.word.wordpack;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WordPackService {

    private final WordPackRepository wordPackRepository;

    public WordPackService(WordPackRepository wordPackRepository) {
        this.wordPackRepository = wordPackRepository;
    }

    public List<WordPackDTO.Response> getAllWordPacks() {
        return wordPackRepository.findAll().stream()
                .map(wordPack -> new WordPackDTO.Response(
                        wordPack.getWordpackId(),
                        wordPack.getName()
                ))
                .collect(Collectors.toList());
    }
}
