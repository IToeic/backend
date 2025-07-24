package kr.mojuk.itoeic.word.word;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    // 특정 단어팩에서 랜덤 5개 단어만 반환
    public List<WordDTO.Response> getRandomWordsByPack(Integer wordpackId) {
        // 해당 단어팩에 포함된 단어만 조회
        List<Word> words = wordRepository.findByWordPack_WordpackId(wordpackId);

        // 단어가 없으면 빈 리스트 반환
        if (words.isEmpty()) {
            return Collections.emptyList();
        }

        // 랜덤 섞기
        Collections.shuffle(words);

        // 5개만 잘라서 DTO 변환
        return words.stream()
                .limit(5)
                .map(WordDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }
}
