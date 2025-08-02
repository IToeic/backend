package kr.mojuk.itoeic.word.progress;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 진행 정보 전송용 DTO
@Getter
@AllArgsConstructor
public class ProgressDTO {
    private int wordpackId;
    private String name;
    private int totalWords;
    private int completeCount;
    private int learningCount;
}
