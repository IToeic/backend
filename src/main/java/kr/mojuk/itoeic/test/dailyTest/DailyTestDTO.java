package kr.mojuk.itoeic.test.dailyTest;

import java.util.List;

import lombok.*;


public class DailyTestDTO {
	
	@Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {  	//사용자ID, 단어ID, 틀린 횟수 가져오기
        private String userId;
        private List<WordResult> words;
        
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class WordResult {	//단어ID, 틀린횟수를 한개의 클래스로 만들기
            private Integer wordId;
            private Integer wrongCount;
        }
    }
}
