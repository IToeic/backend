package kr.mojuk.itoeic.word.word;

import lombok.*;

public class WordDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Integer wordId;
        private String word;
        private String meaning;
        private String voiceUrl;
        private Integer wordpackId;

        public static Response fromEntity(Word word) {
            return Response.builder()
                    .wordId(word.getWordId())
                    .word(word.getWord())
                    .meaning(word.getMeaning())
                    .voiceUrl(word.getVoiceUrl())
                    .wordpackId(word.getWordPack().getWordpackId())
                    .build();
        }
    }
}
