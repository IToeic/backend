package kr.mojuk.itoeic.word.progress;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "progresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Long progressId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "word_id", nullable = false)
    private int wordId;

    @Column(nullable = false)
    private String status; // "learning" or "completed"

    @Builder.Default
    @Column(name = "learn_date")
    private LocalDate learnDate = LocalDate.now();
}
