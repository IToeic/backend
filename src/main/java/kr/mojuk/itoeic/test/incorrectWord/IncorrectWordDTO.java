package kr.mojuk.itoeic.test.incorrectWord;

import java.util.List;

import lombok.*;


public class IncorrectWordDTO {
	
	@Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String userId;
        private List<WordResult> words;
        
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class WordResult {
            private Integer wordId;
            private Integer wrongCount;
        }
    }
}
