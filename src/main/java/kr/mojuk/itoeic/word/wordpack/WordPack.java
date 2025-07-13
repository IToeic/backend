package kr.mojuk.itoeic.word.wordpack;

import jakarta.persistence.*;

@Entity
@Table(name = "wordpacks")
public class WordPack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wordpack_id")
    private Integer wordpackId;

    @Column(name = "name", nullable = false)
    private String name;

    public WordPack() {}

    public Integer getWordpackId() {
        return wordpackId;
    }

    public void setWordpackId(Integer wordpackId) {
        this.wordpackId = wordpackId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
