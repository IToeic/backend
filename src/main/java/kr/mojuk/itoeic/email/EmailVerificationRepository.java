package kr.mojuk.itoeic.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerifications, Long> {
    Optional<EmailVerifications> findByEmail(@Param("email") String email);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM EmailVerifications e WHERE e.email = :email")
    void deleteByEmail(@Param("email") String email);
} 