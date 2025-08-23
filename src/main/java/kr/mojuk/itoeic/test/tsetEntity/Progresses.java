package kr.mojuk.itoeic.test.tsetEntity;

import jakarta.persistence.*;
import kr.mojuk.itoeic.user.Users;
import kr.mojuk.itoeic.word.word.Word;
import lombok.*;

import java.time.LocalDate;

//진행도 테이블
@Entity
@Table(name = "progresses", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "word_id"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Progresses {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Integer progressId;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;
	
	@Builder.Default
	@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.LEARNING;

    @Builder.Default
    @Column(name = "learn_date")
    private LocalDate learnDate = LocalDate.now();

    public enum Status {
        LEARNING,
        COMPLETED
    }
}
