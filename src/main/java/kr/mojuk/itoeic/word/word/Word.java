package kr.mojuk.itoeic.word.word;

import jakarta.persistence.*;
import kr.mojuk.itoeic.word.wordpack.WordPack;

@Entity
@Table(name = "words")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Integer wordId;

    @Column(nullable = false)
    private String word;

    @Column(nullable = false)
    private String meaning;

    @Column(name = "voice_url")
    private String voiceUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wordpack_id", nullable = false)
    private WordPack wordPack;

    public Integer getWordId() {
        return wordId;
    }

    public void setWordId(Integer wordId) {
        this.wordId = wordId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public WordPack getWordPack() {
        return wordPack;
    }

    public void setWordPack(WordPack wordPack) {
        this.wordPack = wordPack;
    }
}
