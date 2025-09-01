package kr.mojuk.itoeic.test.userWords.DTO;

import lombok.Data;
import java.util.List;

@Data
public class UserWordDeleteRequestDto {
    private List<Integer> wordIds;
}
