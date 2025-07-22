package kr.mojuk.itoeic.test.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, String>{
	boolean existsByUserId(String userId);
}
