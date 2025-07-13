package kr.mojuk.itoeic.word.word;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WordService {

    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<WordDTO.Response> getRandomWords() {
        return wordRepository.findRandomFive().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private WordDTO.Response toResponse(Word word) {
        return new WordDTO.Response(
                word.getWordId(),
                word.getWord(),
                word.getMeaning(),
                word.getVoiceUrl(),
                word.getWordPack().getName()  // 여기 수정됨
        );
    }
}
