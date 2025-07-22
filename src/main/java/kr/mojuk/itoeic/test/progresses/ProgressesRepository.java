package kr.mojuk.itoeic.test.progresses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProgressesRepository extends JpaRepository<Progresses, Integer> {

    // 사용자 ID와 단어 ID 리스트로 해당 Progresses 조회
    @Query("SELECT p FROM Progresses p WHERE p.user.id = :userId AND p.word.wordId IN :wordIds")
    List<Progresses> findByUserIdAndWordIds(String userId, List<Integer> wordIds);

    // 학습 완료 상태로 일괄 업데이트 (JPA 벌크 업데이트 방식)
    @Modifying
    @Query("UPDATE Progresses p SET p.status = 'COMPLETED' WHERE p.user.id = :userId AND p.word.wordId IN :wordIds")
    int updateStatusToCompleted(String userId, List<Integer> wordIds);
}
