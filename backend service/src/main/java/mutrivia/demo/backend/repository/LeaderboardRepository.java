package mutrivia.demo.backend.repository;

import mutrivia.demo.backend.model.LeaderboardRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderboardRepository extends CrudRepository<LeaderboardRecord, String> {
    List<LeaderboardRecord> findLeaderboardRecordByMuseumIdOrderByScoreAsc(String museumId);
}
