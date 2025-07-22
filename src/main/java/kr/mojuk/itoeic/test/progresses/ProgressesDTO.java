package kr.mojuk.itoeic.test.progresses;

import java.util.List;

import lombok.*;

public class ProgressesDTO {
	@Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String userId;
        private List<Integer> wordIds;
    }
}
