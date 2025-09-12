package kr.mojuk.itoeic.word.progress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByUserId(String userId);
    
    // 사용자의 단어 학습 상태를 단어팩 기준으로 집계하는 쿼리
    @Query("SELECT " +
            "    new kr.mojuk.itoeic.word.progress.ProgressStatsDTO(w.wordPack.wordpackId, p.status, COUNT(p)) " +
            "FROM Progress p JOIN Word w ON p.wordId = w.wordId " +
            "WHERE p.userId = :userId " +
            "GROUP BY w.wordPack.wordpackId, p.status")
     List<ProgressStatsDTO> findProgressStatsByUserId(@Param("userId") String userId);
}