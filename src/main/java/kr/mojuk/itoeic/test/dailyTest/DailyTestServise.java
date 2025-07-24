package kr.mojuk.itoeic.test.dailyTest;

import java.util.*;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.mojuk.itoeic.test.dailyTest.DailyTestDTO.Request.WordResult;
import kr.mojuk.itoeic.test.testRepository.*;
import kr.mojuk.itoeic.test.tsetEntity.IncorrectWord;
import kr.mojuk.itoeic.user.UsersRepository;
import kr.mojuk.itoeic.word.word.WordRepository;

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
    
    @Transactional
    public void processTestResult(DailyTestDTO.Request request) {
    	var user = usersRepository.findById(request.getUserId()).orElseThrow();
    	
    	// 단어ID 저장용 리스트
        List<Integer> wordIds = new ArrayList<>();
        
        for (WordResult wordResult : request.getWords()) {
        	//단어ID 배열에 저장
            wordIds.add(wordResult.getWordId());
            
            //해당 단어의 틀린 횟수가 1회 이상인 경우(1번이라도 틀렸을 경우)
            if (wordResult.getWrongCount() > 0) {
            	
            	//중복 여부 확인 변수
                boolean alreadyExists = incorrectWordRepository
                        .findByUserIdAndWordId(request.getUserId(), wordResult.getWordId())
                        .isPresent();

                //중복이 아닌 경우
                if (!alreadyExists) {
                    var word = wordRepository.findById(wordResult.getWordId()).orElseThrow();
                    IncorrectWord incorrectWord = IncorrectWord.builder()
                            .user(user)
                            .word(word)
                            .build();
                    
                    //DB에 틀린 단어 저장 
                    incorrectWordRepository.save(incorrectWord);
                }
            }
        }
        //단어들의 상태를 completed로 업데이트
        progressesRepository.updateStatusToCompleted(request.getUserId(), wordIds);
    }
    
}
