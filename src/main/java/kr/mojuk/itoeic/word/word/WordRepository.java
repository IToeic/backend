package kr.mojuk.itoeic.word.word;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WordRepository extends JpaRepository<Word, Integer> {

    // 랜덤으로 n개 가져오는건 서비스에서 처리
    List<Word> findByWordPack_WordpackId(Integer wordpackId);
}
