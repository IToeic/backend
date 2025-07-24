package kr.mojuk.itoeic.word.word;

import jakarta.persistence.*;
import kr.mojuk.itoeic.word.wordpack.WordPack;
import lombok.*;

@Entity
@Table(name = "words")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
