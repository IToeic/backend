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

    @Transactional
    public void addUserWord(UserWordRequestDto request) {
    	Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        Word word = wordRepository.findById(request.getWordId())
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
    public void deleteUserWord(UserWordRequestDto request) {
    	userWordsRepository.deleteByUserIdAndWordId(request.getUserId(), request.getWordId());
    }
}
