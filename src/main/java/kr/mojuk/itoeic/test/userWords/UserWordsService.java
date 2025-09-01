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
        // null 체크 추가
        if (userId == null) {
            throw new IllegalArgumentException("userId는 null일 수 없습니다.");
        }
        if (wordId == null) {
            throw new IllegalArgumentException("wordId는 null일 수 없습니다.");
        }
        
        System.out.println("addUserWord 호출됨 - userId: " + userId + ", wordId: " + wordId);
        
    	Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음: " + userId));

        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new IllegalArgumentException("단어 없음: " + wordId));
        
        boolean exists = userWordsRepository.findByUserIdAndWordId(user.getUserId(), word.getWordId()).isPresent();

        if (!exists) {
        	UserWords userWord = UserWords.builder()
                    .user(user)
                    .word(word)
                    .build();
            userWordsRepository.save(userWord);
            System.out.println("단어가 성공적으로 추가됨");
        } else {
            System.out.println("이미 존재하는 단어입니다.");
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
