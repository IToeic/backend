package kr.mojuk.itoeic.test.dailyTest;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WordIdListDTO {
    private List<Integer> wordIds;
}