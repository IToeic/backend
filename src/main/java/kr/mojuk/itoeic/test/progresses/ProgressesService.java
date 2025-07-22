package kr.mojuk.itoeic.test.progresses;

import jakarta.transaction.Transactional;
import kr.mojuk.itoeic.test.user.UsersRepository;
import kr.mojuk.itoeic.word.word.WordRepository;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProgressesService {

    private final ProgressesRepository progressesRepository;
    private final UsersRepository usersRepository;
    private final WordRepository wordRepository;

    public ProgressesService(ProgressesRepository progressesRepository, UsersRepository usersRepository, WordRepository wordRepository) {
        this.progressesRepository = progressesRepository;
        this.usersRepository = usersRepository;
        this.wordRepository = wordRepository;
    }

    @Transactional
    public void markWordsAsCompleted(ProgressesDTO.Request request) {
        progressesRepository.updateStatusToCompleted(request.getUserId(), request.getWordIds());
    }
}
