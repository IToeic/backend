package kr.mojuk.itoeic.test.wordpackTest;


import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.mojuk.itoeic.test.testDTO.TestResultRequest;
import kr.mojuk.itoeic.test.testDTO.WordResult;
import kr.mojuk.itoeic.test.testRepository.*;
import kr.mojuk.itoeic.test.tsetEntity.IncorrectWord;
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
    public void processTestResult(String userId, TestResultRequest request) {
    	var user = usersRepository.findById(userId).orElseThrow(); // 예외 처리는 나중에 개선

        // 1. 요청으로 들어온 모든 '틀린 단어'의 ID 리스트를 미리 추출합니다.
        List<Integer> wrongWordIdsFromRequest = request.getWords().stream()
                .filter(word -> word.getWrongCount() > 0)
                .map(WordResult::getWordId) // 'WordResult'를 인식하기 위해 import가 필요합니다.
                .collect(Collectors.toList());

        // 만약 틀린 단어가 없다면, 더 이상 진행할 필요가 없으므로 여기서 종료합니다.
        if (wrongWordIdsFromRequest.isEmpty()) {
            return;
        }

        // 2. DB에 딱 한 번만 조회해서, 이미 저장된 단어 ID들을 가져옵니다.
        Set<Integer> existingIncorrectWordIds = incorrectWordRepository
                .findWordIdsByUserIdAndWordIdIn(userId, wrongWordIdsFromRequest);

        // 3. DB에 새로 저장해야 할 단어들을 찾아 엔티티 리스트로 만듭니다.
        List<IncorrectWord> newIncorrectWords = wrongWordIdsFromRequest.stream()
                // 4. DB 조회가 아닌, 메모리에 있는 Set을 통해 중복 여부를 확인합니다.
                .filter(wordId -> !existingIncorrectWordIds.contains(wordId))
                .map(wordId -> {
                    var word = wordRepository.findById(wordId).orElseThrow();
                    return IncorrectWord.builder()
                            .user(user)
                            .word(word)
                            .build();
                })
                .collect(Collectors.toList());

        // 5. 새로 추가할 단어들을 DB에 한 번의 쿼리로 모두 저장합니다.
        if (!newIncorrectWords.isEmpty()) {
            incorrectWordRepository.saveAll(newIncorrectWords);
        }
    }
}