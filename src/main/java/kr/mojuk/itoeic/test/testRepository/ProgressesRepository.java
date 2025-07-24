package kr.mojuk.itoeic.test.testRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.mojuk.itoeic.test.tsetEntity.Progresses;

import java.util.List;

public interface ProgressesRepository extends JpaRepository<Progresses, Integer> {

    // 사용자ID와 단어ID 리스트로 해당 Progresses 조회
    @Query("SELECT p FROM Progresses p WHERE p.user.userId = :userId AND p.word.wordId IN :wordIds")
    List<Progresses> findByUserIdAndWordIds(@Param("userId")String userId, @Param("wordIds")List<Integer> wordIds);

    // 학습 완료 상태로 일괄 업데이트 (JPA 벌크 업데이트 방식)
    @Modifying
    @Query("UPDATE Progresses p SET p.status = 'COMPLETED' WHERE p.user.userId = :userId AND p.word.wordId IN :wordIds")
    int updateStatusToCompleted(@Param("userId")String userId, @Param("wordIds") List<Integer> wordIds);
}
