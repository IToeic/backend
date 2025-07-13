package kr.mojuk.itoeic.word.wordpack;

public class WordPackDTO {

    public static class Response {
        private Integer id;
        private String name;

        public Response(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
