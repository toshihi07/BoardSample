package boardSample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import boardSample.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
	@Query("SELECT u FROM User u WHERE u.username = :username")
	User findByUsername(@Param("username") String username);
	
	@Query("SELECT u FROM User u WHERE u.userId = :userId")
	User findByUserId(@Param("userId") int user_id);
	
	int countByUsername(String username);
}
