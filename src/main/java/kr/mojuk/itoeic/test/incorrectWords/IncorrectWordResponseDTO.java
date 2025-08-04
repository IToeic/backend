package kr.mojuk.itoeic.test.incorrectWords;

import kr.mojuk.itoeic.test.tsetEntity.IncorrectWord;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncorrectWordResponseDTO {
    private Integer IncorrectWordId; 
    private String word;
    private String meaning;

    public static IncorrectWordResponseDTO fromEntity(IncorrectWord entity) {
        return IncorrectWordResponseDTO.builder()
                .IncorrectWordId(entity.getIncorrectWordId())
                .word(entity.getWord().getWord())
                .meaning(entity.getWord().getMeaning())
                .build();
    }
}

