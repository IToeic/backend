package kr.mojuk.itoeic.test.testDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WordResult {
    private Integer wordId;
    private Integer wrongCount;
}