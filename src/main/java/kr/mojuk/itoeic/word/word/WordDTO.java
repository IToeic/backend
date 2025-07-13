package kr.mojuk.itoeic.word.word;

public class WordDTO {

    // ✅ 단어 조회용 응답 DTO
    public static class Response {
        private Integer id;
        private String word;
        private String meaning;
        private String voiceUrl;
        private String wordPackName; // 단어팩 이름

        // 🔧 생성자
        public Response(Integer id, String word, String meaning, String voiceUrl, String wordPackName) {
            this.id = id;
            this.word = word;
            this.meaning = meaning;
            this.voiceUrl = voiceUrl;
            this.wordPackName = wordPackName;
        }

        // 🔍 Getter 메서드들
        public Integer getId() {
            return id;
        }

        public String getWord() {
            return word;
        }

        public String getMeaning() {
            return meaning;
        }

        public String getVoiceUrl() {
            return voiceUrl;
        }

        public String getWordPackName() {
            return wordPackName;
        }
    }
}
