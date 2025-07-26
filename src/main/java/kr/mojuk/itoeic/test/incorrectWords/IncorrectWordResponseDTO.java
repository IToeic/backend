package kr.mojuk.itoeic.test.incorrectWords;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncorrectWordResponseDTO {
	private String word;
    private String meaning;

    public static IncorrectWordResponseDTO fromEntity(kr.mojuk.itoeic.test.tsetEntity.IncorrectWord entity) {
        return IncorrectWordResponseDTO.builder()
                .word(entity.getWord().getWord())
                .meaning(entity.getWord().getMeaning())
                .build();
    }
}
