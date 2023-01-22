package mutrivia.demo.backend.repository;

import mutrivia.demo.backend.model.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends CrudRepository<Session, String> {
    Session findByHostId(String hostId);
}
