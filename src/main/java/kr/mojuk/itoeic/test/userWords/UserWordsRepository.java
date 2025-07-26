package kr.mojuk.itoeic.test.userWords;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import kr.mojuk.itoeic.word.word.Word;

@Repository
public interface UserWordsRepository extends JpaRepository<UserWords, Integer> {

    @Query("SELECT uw.word FROM UserWords uw WHERE uw.user.userId = :userId")
    List<Word> findWordsByUserId(@Param("userId")String userId);

    @Query("SELECT uw FROM UserWords uw WHERE uw.user.userId = :userId AND uw.word.wordId = :wordId")
    Optional<UserWords> findByUserIdAndWordId(@Param("userId")String userId, @Param("wordId")Integer wordId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserWords uw WHERE uw.user.userId = :userId AND uw.word.wordId = :wordId")
    void deleteByUserIdAndWordId(@Param("userId")String userId, @Param("wordId")Integer wordId);
}