package kr.mojuk.itoeic.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, String>{
	boolean existsByUserId(@Param("userId")String userId);
	Optional<Users> findByUserId(@Param("userId")String userId);
	boolean existsByEmail(@Param("email")String email);
}
