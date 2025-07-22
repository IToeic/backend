package kr.mojuk.itoeic.test.incorrectWord;

import jakarta.transaction.Transactional;
import kr.mojuk.itoeic.test.user.UsersRepository;
import kr.mojuk.itoeic.word.word.WordRepository;

import org.springframework.stereotype.Service;

@Service
public class IncorrectWordService {

    private final IncorrectWordRepository incorrectWordRepository;
    private final UsersRepository usersRepository;
    private final WordRepository wordRepository;

    public IncorrectWordService(IncorrectWordRepository incorrectWordRepository,
                                 UsersRepository usersRepository,
                                 WordRepository wordRepository) {
        this.incorrectWordRepository = incorrectWordRepository;
        this.usersRepository = usersRepository;
        this.wordRepository = wordRepository;
    }

    @Transactional
    public void processIncorrectWords(IncorrectWordDTO.Request request) {
        var user = usersRepository.findById(request.getUserId()).orElseThrow();

        for (IncorrectWordDTO.Request.WordResult wordResult : request.getWords()) {
            if (wordResult.getWrongCount() > 0) {
                boolean alreadyExists = incorrectWordRepository
                        .findByUserIdAndWordId(request.getUserId(), wordResult.getWordId())
                        .isPresent();

                if (!alreadyExists) {
                    var word = wordRepository.findById(wordResult.getWordId()).orElseThrow();

                    IncorrectWord incorrectWord = IncorrectWord.builder()
                            .user(user)
                            .word(word)
                            .build();

                    incorrectWordRepository.save(incorrectWord);
                }
            }
        }
    }
}