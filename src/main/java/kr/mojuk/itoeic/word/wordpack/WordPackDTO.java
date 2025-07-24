package kr.mojuk.itoeic.word.wordpack;

import lombok.*;

public class WordPackDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Integer wordpackId;
        private String name;

        public static Response fromEntity(WordPack pack) {
            return Response.builder()
                    .wordpackId(pack.getWordpackId())
                    .name(pack.getName())
                    .build();
        }
    }
}
