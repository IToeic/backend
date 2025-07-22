package kr.mojuk.itoeic.test.incorrectWord;

import jakarta.persistence.*;
import kr.mojuk.itoeic.word.word.Word;
import kr.mojuk.itoeic.test.user.Users;
import lombok.*;

@Entity
@Table(name = "incorrect_words", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "word_id"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncorrectWord {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "incorrect_word_id")
    private Integer incorrectWordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;
}
