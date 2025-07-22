package kr.mojuk.itoeic.test.incorrectWord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IncorrectWordRepository extends JpaRepository<IncorrectWord, Integer> {

    // 중복 체크용
    @Query("SELECT iw FROM IncorrectWord iw WHERE iw.user.id = :userId AND iw.word.wordId = :wordId")
    Optional<IncorrectWord> findByUserIdAndWordId(String userId, Integer wordId);
}
