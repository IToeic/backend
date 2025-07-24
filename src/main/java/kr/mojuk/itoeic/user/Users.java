package kr.mojuk.itoeic.user;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {
	@Id
    @Column(name = "user_id", length = 50)
    private String userId;
	
	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
	@Column(name = "password_hash", nullable = false, length = 255)
	private String passwordHash;
	
	@Column(nullable = false, length = 100)
    private String email;

	@Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "is_deleted", columnDefinition = "TINYINT DEFAULT 0")
    private Boolean isDeleted;
}
