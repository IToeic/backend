package kr.mojuk.itoeic.word.wordpack;

import kr.mojuk.itoeic.word.progress.Progress;
import kr.mojuk.itoeic.word.progress.ProgressDTO;
import kr.mojuk.itoeic.word.progress.ProgressRepository;
import kr.mojuk.itoeic.word.progress.ProgressStatsDTO;
import kr.mojuk.itoeic.word.word.Word;
import kr.mojuk.itoeic.word.word.WordCountDTO;
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
        // 1. 모든 단어팩 정보 조회 (기본 정보)
        List<WordPack> wordPacks = wordPackRepository.findAll();
        
        // 2. 단어팩별 총 단어 수 (DB에서 계산)
        List<WordCountDTO> wordCounts = wordRepository.countWordsByWordPack();
        Map<Integer, Long> totalWordsMap = wordCounts.stream()
                .collect(Collectors.toMap(WordCountDTO::getId, WordCountDTO::getCount));
        
        // 3. 사용자의 학습 상태 통계 (DB에서 계산)
        Map<Integer, Map<String, Long>> progressStatsMap = 
            progressRepository.findProgressStatsByUserId(userId)
                .stream()
                .collect(Collectors.groupingBy(
                    ProgressStatsDTO::getWordpackId,
                    Collectors.toMap(
                        stats -> stats.getStatus().toLowerCase(),
                        ProgressStatsDTO::getCount
                    )
                ));

        // 4. 메모리에서 세 종류의 데이터를 최종적으로 조합
        return wordPacks.stream()
                .map(wp -> {
                    int wordpackId = wp.getWordpackId();
                    Map<String, Long> packProgress = progressStatsMap.getOrDefault(wordpackId, Map.of());
                    
                    int totalWords = totalWordsMap.getOrDefault(wordpackId, 0L).intValue();
                    int completeCount = packProgress.getOrDefault("completed", 0L).intValue();

                    return new ProgressDTO(
                            wordpackId,
                            wp.getName(),
                            totalWords,
                            completeCount
                    );
                })
                .collect(Collectors.toList());
    }
}
