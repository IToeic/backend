package kr.mojuk.itoeic.word.word;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WordRepository extends JpaRepository<Word, Integer> {

    // 랜덤으로 n개 가져오는건 서비스에서 처리
    List<Word> findByWordPack_WordpackId(Integer wordpackId);
   
    // 여러 wordpack ID에 해당하는 모든 단어를 조회하는 메서드 추가
    List<Word> findAllByWordPack_WordpackIdIn(List<Integer> wordpackIds);
    
    // DB에 접근하여 5개 조회
    @Query(value = "SELECT w FROM Word w WHERE w.wordPack.wordpackId = :wordpackId ORDER BY FUNCTION('RAND') LIMIT 5")
    List<Word> findRandom5ByWordPackId(@Param("wordpackId") Integer wordpackId);
    
    // 특정 단어 ID들을 제외하고 랜덤으로 N개 조회
    @Query(value = "SELECT w FROM Word w WHERE w.wordPack.wordpackId = :wordpackId AND w.wordId NOT IN :excludedWordIds ORDER BY FUNCTION('RAND') LIMIT :limit")
    List<Word> findRandomWordsByPackIdExcludingIds(
            @Param("wordpackId") Integer wordpackId,
            @Param("excludedWordIds") List<Integer> excludedWordIds,
            @Param("limit") int limit
    );
}
