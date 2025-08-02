package kr.mojuk.itoeic.word.wordpack;

import kr.mojuk.itoeic.word.progress.Progress;
import kr.mojuk.itoeic.word.progress.ProgressDTO;
import kr.mojuk.itoeic.word.progress.ProgressRepository;
import kr.mojuk.itoeic.word.word.Word;
import kr.mojuk.itoeic.word.word.WordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WordPackService {

    private final WordPackRepository wordPackRepository;
    private final WordRepository wordRepository;
    private final ProgressRepository progressRepository;

    public WordPackService(WordPackRepository wordPackRepository,
                           WordRepository wordRepository,
                           ProgressRepository progressRepository) {
        this.wordPackRepository = wordPackRepository;
        this.wordRepository = wordRepository;
        this.progressRepository = progressRepository;
    }

    public List<ProgressDTO> getWordPackProgressList(String userId) {
        List<WordPack> wordPacks = wordPackRepository.findAll();
        List<Progress> progresses = progressRepository.findByUserId(userId);
        List<Word> allWords = wordRepository.findAll();

        // wordId → wordPackId 매핑
        Map<Integer, Integer> wordIdToPackId = allWords.stream()
                .collect(Collectors.toMap(
                        Word::getWordId,
                        w -> w.getWordPack().getWordpackId()
                ));

        // wordPackId → 전체 단어 수
        Map<Integer, Long> wordPackIdToTotalCount = allWords.stream()
                .collect(Collectors.groupingBy(
                        w -> w.getWordPack().getWordpackId(),
                        Collectors.counting()
                ));

        return wordPacks.stream()
                .map(wp -> {
                    int wordpackId = wp.getWordpackId();

                    int completeCount = (int) progresses.stream()
                            .filter(p -> {
                                Integer packId = wordIdToPackId.get(p.getWordId());
                                return packId != null && packId == wordpackId &&
                                        "completed".equalsIgnoreCase(p.getStatus());
                            })
                            .count();

                    int learningCount = (int) progresses.stream()
                            .filter(p -> {
                                Integer packId = wordIdToPackId.get(p.getWordId());
                                return packId != null && packId == wordpackId &&
                                        "learning".equalsIgnoreCase(p.getStatus());
                            })
                            .count();

                    int totalWords = wordPackIdToTotalCount.getOrDefault(wordpackId, 0L).intValue();

                    return new ProgressDTO(
                            wordpackId,
                            wp.getName(),
                            totalWords,
                            completeCount,
                            learningCount
                    );
                })
                .collect(Collectors.toList());
    }
}
