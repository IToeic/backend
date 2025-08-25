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

    // 특정 단어팩에서 랜덤 5개 단어만 반환합니다.
    public List<WordDTO.Response> getRandomWordsByPack(Integer wordpackId) {
        List<Word> words = wordRepository.findRandom5ByWordPackId(wordpackId);
        return words.stream()
                .map(WordDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 단어팩의 모든 단어를 반환합니다. (순서 섞음)
    public List<WordDTO.Response> getAllWordsByPack(Integer wordpackId) {
        List<Word> words = wordRepository.findByWordPack_WordpackId(wordpackId);
        Collections.shuffle(words); 
        return words.stream()
                .map(WordDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    // 여러 단어팩 ID를 받아 해당하는 모든 단어를 반환합니다. (순서 섞음)
    public List<WordDTO.Response> getAllWordsByPackIds(List<Integer> wordpackIds) {
        List<Word> words = wordRepository.findAllByWordPack_WordpackIdIn(wordpackIds);
        Collections.shuffle(words); // 이 부분도 일관성을 위해 추가했습니다.
        return words.stream()
                .map(WordDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }
}
