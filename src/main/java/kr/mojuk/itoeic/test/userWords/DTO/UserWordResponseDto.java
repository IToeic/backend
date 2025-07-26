package kr.mojuk.itoeic.test.userWords.DTO;


import kr.mojuk.itoeic.word.word.Word;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWordResponseDto {
    private Integer wordId;
    private String word;
    private String meaning; // 필요한 필드만

    public static UserWordResponseDto fromEntity(Word word) {
        return new UserWordResponseDto(
            word.getWordId(),
            word.getWord(),        // Word 엔티티 안에 단어 문자열
            word.getMeaning()     // 뜻 등
        );
    }
}

