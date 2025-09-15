package kr.mojuk.itoeic.test.dailyTest;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.mojuk.itoeic.test.testDTO.TestResultRequest;
import kr.mojuk.itoeic.test.testDTO.WordResult;
import kr.mojuk.itoeic.test.testRepository.*;
import kr.mojuk.itoeic.test.tsetEntity.IncorrectWord;
import kr.mojuk.itoeic.test.tsetEntity.Progresses;
import kr.mojuk.itoeic.user.UsersRepository;
import kr.mojuk.itoeic.word.word.WordRepository;

import java.time.LocalDate;

@Service
public class DailyTestServise {
	private final ProgressesRepository progressesRepository;
	private final IncorrectWordRepository incorrectWordRepository;
    private final UsersRepository usersRepository;
    private final WordRepository wordRepository;
    
    public DailyTestServise(IncorrectWordRepository incorrectWordRepository,
    						 ProgressesRepository progressesRepository,
    						 UsersRepository usersRepository,
    						 WordRepository wordRepository) {
		this.incorrectWordRepository = incorrectWordRepository;
		this.progressesRepository = progressesRepository;
		this.usersRepository = usersRepository;
		this.wordRepository = wordRepository;
    }
    
    // 1. 단어 세트를 PENDING 상태로 저장하는 메소드
    @Transactional
    public void createPendingWords(String userId, List<Integer> wordIds) {
        var user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. ID: " + userId));

        List<Progresses> newProgresses = new ArrayList<>();
        for (Integer wordId : wordIds) {
            // 중복 저장을 방지하기 위해 이미 해당 단어에 대한 진행 기록이 있는지 확인합니다.
            boolean exists = !progressesRepository.findByUserIdAndWordIds(userId, List.of(wordId)).isEmpty();
            if (!exists) {
                var word = wordRepository.findById(wordId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 단어를 찾을 수 없습니다. ID: " + wordId));
                
                Progresses progress = Progresses.builder()
                        .user(user)
                        .word(word)
                        .build();
                
                newProgresses.add(progress);
            }
        }

        if (!newProgresses.isEmpty()) {
            progressesRepository.saveAll(newProgresses);
        }
    }
    
    @Transactional
    public void processTestResult(String userId, TestResultRequest request) {
    	var user = usersRepository.findById(userId).orElseThrow(); // 예외 처리는 나중에 개선

    	// 1. 틀린 단어 처리
        // 요청에 포함된 단어 중 틀린 것들의 ID만 추출
        List<Integer> wrongWordIdsFromRequest = request.getWords().stream()
                .filter(word -> word.getWrongCount() > 0)
                .map(WordResult::getWordId)
                .collect(Collectors.toList());

        // 틀린 단어가 있는 경우에만 DB에 조회 및 저장 로직 실행
        if (!wrongWordIdsFromRequest.isEmpty()) {
            // DB에 딱 한 번만 조회해서 이미 저장된 '틀린 단어' ID들을 가져옴
            Set<Integer> existingIncorrectWordIds = incorrectWordRepository
                    .findWordIdsByUserIdAndWordIdIn(userId, wrongWordIdsFromRequest);

            // 메모리에서 비교하여 DB에 새로 저장해야 할 단어 엔티티 리스트를 생성
            List<IncorrectWord> newIncorrectWords = wrongWordIdsFromRequest.stream()
                    .filter(wordId -> !existingIncorrectWordIds.contains(wordId))
                    .map(wordId -> {
                        var word = wordRepository.findById(wordId).orElseThrow();
                        return IncorrectWord.builder()
                                .user(user)
                                .word(word)
                                .build();
                    })
                    .collect(Collectors.toList());

            // 새로 추가할 단어들을 DB에 한 번의 쿼리로 모두 저장
            if (!newIncorrectWords.isEmpty()) {
                incorrectWordRepository.saveAll(newIncorrectWords);
            }
        }

        // 2. 학습 진행 상태(Progresses) 'COMPLETED'로 일괄 업데이트 
        // 요청에 포함된 '모든' 단어의 ID 리스트를 추출
        List<Integer> allWordIds = request.getWords().stream()
                                          .map(WordResult::getWordId)
                                          .collect(Collectors.toList());

        // 단어 ID 리스트가 비어있지 않다면, Progresses 상태를 'COMPLETED'로 일괄 업데이트
        if (!allWordIds.isEmpty()) {
            progressesRepository.updateStatusToCompleted(userId, allWordIds);
        }
    }
    
    @Transactional
    public void updateStatusToLearning(String userId, Integer wordId) {
        progressesRepository.updateStatusToLearning(userId, wordId);
    }
    
}
