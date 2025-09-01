package kr.mojuk.itoeic.test.testRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.mojuk.itoeic.test.tsetEntity.Progresses;

import java.time.LocalDate;
import java.util.List;

public interface ProgressesRepository extends JpaRepository<Progresses, Integer> {

    // 사용자ID와 단어ID 리스트로 해당 Progresses 조회
    @Query("SELECT p FROM Progresses p WHERE p.user.userId = :userId AND p.word.wordId IN :wordIds")
    List<Progresses> findByUserIdAndWordIds(@Param("userId")String userId, @Param("wordIds")List<Integer> wordIds);
    
    // [추가] PENDING 상태인 단어를 LEARNING 상태로 업데이트
    @Modifying
    @Query("UPDATE Progresses p SET p.status = 'LEARNING' WHERE p.user.userId = :userId AND p.word.wordId = :wordId AND p.status = 'PENDING'")
    int updateStatusToLearning(@Param("userId") String userId, @Param("wordId") Integer wordId);

    // 학습 완료 상태로 일괄 업데이트 (JPA 벌크 업데이트 방식)
    @Modifying
    @Query("UPDATE Progresses p SET p.status = 'COMPLETED' WHERE p.user.userId = :userId AND p.word.wordId IN :wordIds")
    int updateStatusToCompleted(@Param("userId")String userId, @Param("wordIds") List<Integer> wordIds);
    
    //사용자 ID와 학습 날짜로 Progresses 조회
    List<Progresses> findByUser_UserIdAndLearnDateAndStatusNot(String userId, LocalDate learnDate, Progresses.Status status);
    

}
