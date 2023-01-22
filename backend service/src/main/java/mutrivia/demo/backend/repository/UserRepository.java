package mutrivia.demo.backend.repository;

import mutrivia.demo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    List<User> findByUsername(String username);
    List<User> findBySessionId(String sessionId);
}
