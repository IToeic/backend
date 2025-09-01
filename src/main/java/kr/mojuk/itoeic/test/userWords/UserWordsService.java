package kr.mojuk.itoeic.test.userWords;


import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.mojuk.itoeic.test.userWords.DTO.*;
import kr.mojuk.itoeic.user.Users;
import kr.mojuk.itoeic.user.UsersRepository;
import kr.mojuk.itoeic.word.word.Word;
import kr.mojuk.itoeic.word.word.WordRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserWordsService {

    private final UsersRepository usersRepository;
    private final WordRepository wordRepository;
    private final UserWordsRepository userWordsRepository;

    @Transactional(readOnly = true)
    public List<UserWordResponseDto> getUserWords(String userId) {
        return userWordsRepository.findWordsByUserId(userId).stream()
                .map(UserWordResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // ✨ addUserWord 메서드 시그니처 변경
    @Transactional
    public void addUserWord(String userId, Integer wordId) {
    	Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new IllegalArgumentException("단어 없음"));
        
        boolean exists = userWordsRepository.findByUserIdAndWordId(user.getUserId(), word.getWordId()).isPresent();

        if (!exists) {
        	UserWords userWord = UserWords.builder()
                    .user(user)
                    .word(word)
                    .build();
            userWordsRepository.save(userWord);
        }
    }

    @Transactional
    public void deleteUserWords(String userId, List<Integer> wordIds) {
    	if (wordIds == null || wordIds.isEmpty()) {
            return;
        }
    	userWordsRepository.deleteByUserIdAndWordIds(userId, wordIds);
    }
}
