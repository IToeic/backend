package kr.mojuk.itoeic.test.userWords;

import jakarta.persistence.*;
import kr.mojuk.itoeic.user.Users;
import kr.mojuk.itoeic.word.word.Word;
import lombok.*;

@Entity
@Table(name = "user_words", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "word_id"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_word_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

}

