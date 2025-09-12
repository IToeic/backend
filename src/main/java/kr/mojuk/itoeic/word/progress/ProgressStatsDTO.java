package kr.mojuk.itoeic.word.progress;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProgressStatsDTO {
    private int wordpackId;
    private String status;
    private long count;
}