package kr.mojuk.itoeic.test.testRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.mojuk.itoeic.test.tsetEntity.IncorrectWord;

import java.util.Optional;

public interface IncorrectWordRepository extends JpaRepository<IncorrectWord, Integer> {

    // 중복 체크용
    @Query("SELECT iw FROM IncorrectWord iw WHERE iw.user.userId = :userId AND iw.word.wordId = :wordId")
    Optional<IncorrectWord> findByUserIdAndWordId(@Param("userId") String userId, @Param("wordId") Integer wordId);
}
