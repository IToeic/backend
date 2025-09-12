// WordCountDTO.java

package kr.mojuk.itoeic.word.word;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // JPQL의 new 생성자 호출을 위해 필요합니다.
public class WordCountDTO {
    private Integer id;
    private Long count;
}