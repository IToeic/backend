package kr.mojuk.itoeic.test.testDTO;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TestResultRequest {
    private List<WordResult> words;
}