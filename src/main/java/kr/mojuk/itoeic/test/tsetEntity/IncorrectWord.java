package kr.mojuk.itoeic.test.tsetEntity;

import jakarta.persistence.*;
import kr.mojuk.itoeic.user.Users;
import kr.mojuk.itoeic.word.word.Word;
import lombok.*;


//틀린단어 테이블
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
