package kr.mojuk.itoeic.test.wordpackTest;


import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.mojuk.itoeic.test.testRepository.*;
import kr.mojuk.itoeic.test.tsetEntity.IncorrectWord;
import kr.mojuk.itoeic.test.wordpackTest.WordpackTestDTO.Request.WordResult;
import kr.mojuk.itoeic.user.UsersRepository;
import kr.mojuk.itoeic.word.word.WordRepository;

@Service
public class WordpackTestServise {

	private final IncorrectWordRepository incorrectWordRepository;
    private final UsersRepository usersRepository;
    private final WordRepository wordRepository;
    
    public WordpackTestServise(IncorrectWordRepository incorrectWordRepository,
    						 UsersRepository usersRepository,
    						 WordRepository wordRepository) {
		this.incorrectWordRepository = incorrectWordRepository;
		this.usersRepository = usersRepository;
		this.wordRepository = wordRepository;
    }
    
    @Transactional
    public void processTestResult(WordpackTestDTO.Request request) {
    	var user = usersRepository.findById(request.getUserId()).orElseThrow();
        
        for (WordResult wordResult : request.getWords()) {
            
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
    }
}
