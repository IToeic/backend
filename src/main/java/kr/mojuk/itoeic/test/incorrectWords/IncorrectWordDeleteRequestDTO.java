package kr.mojuk.itoeic.test.incorrectWords;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncorrectWordDeleteRequestDTO {
    private List<Integer> wordIds;
}
