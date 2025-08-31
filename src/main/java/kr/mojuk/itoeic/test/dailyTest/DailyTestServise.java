package kr.mojuk.itoeic.test.dailyTest;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.mojuk.itoeic.test.dailyTest.DailyTestDTO.Request.WordResult;
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
                        .build(); // status는 엔티티에서 PENDING으로 기본 설정됨
                
                newProgresses.add(progress);
            }
        }

        if (!newProgresses.isEmpty()) {
            progressesRepository.saveAll(newProgresses);
        }
    }
    
    @Transactional
    public void processTestResult(String userId, DailyTestDTO.Request request) {
    	var user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. ID: " + userId));

        // 1. 틀린 단어(wrongCount > 0)만 필터링하여 IncorrectWord 테이블에 저장합니다.
        for (WordResult wordResult : request.getWords()) {
            if (wordResult.getWrongCount() > 0) {
                // 이미 틀린 단어로 등록되어 있는지 중복 확인
                boolean alreadyExists = incorrectWordRepository
                        .findByUserIdAndWordId(userId, wordResult.getWordId())
                        .isPresent();

                // 중복이 아닐 경우에만 새로 저장
                if (!alreadyExists) {
                    var word = wordRepository.findById(wordResult.getWordId())
                            .orElseThrow(() -> new IllegalArgumentException("해당 단어를 찾을 수 없습니다. ID: " + wordResult.getWordId()));
                    
                    IncorrectWord incorrectWord = IncorrectWord.builder()
                            .user(user)
                            .word(word)
                            .build();
                    
                    incorrectWordRepository.save(incorrectWord);
                }
            }
        }
        
        // 2. 요청에 포함된 모든 단어의 ID를 리스트로 추출합니다.
        List<Integer> allWordIds = request.getWords().stream()
                                          .map(WordResult::getWordId)
                                          .collect(Collectors.toList());

        // 3. 단어 ID 리스트가 비어있지 않다면, Progresses 상태를 'COMPLETED'로 일괄 업데이트합니다.
        if (!allWordIds.isEmpty()) {
            progressesRepository.updateStatusToCompleted(userId, allWordIds);
        }
    }
    
    @Transactional
    public void updateStatusToLearning(String userId, Integer wordId) {
        progressesRepository.updateStatusToLearning(userId, wordId);
    }
    
}
