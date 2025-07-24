package kr.mojuk.itoeic.word.wordpack;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wordpacks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordPack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wordpack_id")
    private Integer wordpackId;

    @Column(nullable = false)
    private String name;
}
