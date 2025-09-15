package kr.mojuk.itoeic.test.testRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import kr.mojuk.itoeic.test.tsetEntity.IncorrectWord;

import java.util.*;

public interface IncorrectWordRepository extends JpaRepository<IncorrectWord, Integer> {

    // 중복 체크용
    @Query("SELECT iw FROM IncorrectWord iw WHERE iw.user.userId = :userId AND iw.word.wordId = :wordId")
    Optional<IncorrectWord> findByUserIdAndWordId(@Param("userId") String userId, @Param("wordId") Integer wordId);
    
    List<IncorrectWord> findByUser_UserId(String userId);

    // userId와 wordId 목록으로 안전하게 삭제하는 쿼리
    @Transactional
    @Modifying
    @Query("DELETE FROM IncorrectWord iw WHERE iw.user.userId = :userId AND iw.word.wordId IN :wordIds")
    void deleteByUserIdAndWordIdIn(@Param("userId") String userId, @Param("wordIds") List<Integer> wordIds);
    
    long countByUser_UserIdAndWord_WordPack_WordpackId(String userId, Integer packId);
    
    @Query("SELECT iw.word.wordId FROM IncorrectWord iw WHERE iw.user.userId = :userId AND iw.word.wordId IN :wordIds")
    Set<Integer> findWordIdsByUserIdAndWordIdIn(@Param("userId") String userId, @Param("wordIds") List<Integer> wordIds);
}
