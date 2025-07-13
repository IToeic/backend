package kr.mojuk.itoeic.word.word;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Integer> {

    // ✅ 랜덤 5개 단어 조회 (native SQL)
    @Query(value = "SELECT * FROM words ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Word> findRandomFive();
}
