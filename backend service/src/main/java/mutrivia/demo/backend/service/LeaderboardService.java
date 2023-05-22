package mutrivia.demo.backend.service;

import lombok.extern.log4j.Log4j;
import mutrivia.demo.backend.model.LeaderboardRecord;
import mutrivia.demo.backend.model.User;
import mutrivia.demo.backend.repository.LeaderboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j
public class LeaderboardService {
    @Autowired
    LeaderboardRepository leaderboardRepository;

    public LeaderboardService(LeaderboardRepository leaderboardRepository) {
        this.leaderboardRepository = leaderboardRepository;
    }

    public List<LeaderboardRecord> getLeaderboardRecords(String museumId){
        return leaderboardRepository.findLeaderboardRecordByMuseumIdOrderByScoreAsc(museumId);
    }

    public void checkIfHighScore(User user){
        List<LeaderboardRecord> leaderboardRecordList = leaderboardRepository.findLeaderboardRecordByMuseumIdOrderByScoreAsc(user.getMuseumId());

        if(leaderboardRecordList.size()<10){
            System.out.println("Add to leaderboard");
            LeaderboardRecord newLeaderBoardRecord = new LeaderboardRecord();
            newLeaderBoardRecord.setUserId(user.getUserId());
            newLeaderBoardRecord.setMuseumId(user.getMuseumId());
            newLeaderBoardRecord.setUsername(user.getUsername());
            newLeaderBoardRecord.setScore(user.getScore());
            leaderboardRepository.save(newLeaderBoardRecord);
        }
        else if(leaderboardRecordList.size() != 0){
            if(leaderboardRecordList.get(0).getScore() < user.getScore()){
                System.out.println("Save in leaderboard");
                leaderboardRepository.delete(leaderboardRecordList.get(0));
                LeaderboardRecord newLeaderBoardRecord = new LeaderboardRecord();
                newLeaderBoardRecord.setUserId(user.getUserId());
                newLeaderBoardRecord.setMuseumId(user.getMuseumId());
                newLeaderBoardRecord.setUsername(user.getUsername());
                newLeaderBoardRecord.setScore(user.getScore());
                leaderboardRepository.save(newLeaderBoardRecord);
            }
        }
    }

    public void deleteAll(List<LeaderboardRecord> leaderboardRecordList){
        for(LeaderboardRecord leaderboardRecord: leaderboardRecordList){
            leaderboardRepository.delete(leaderboardRecord);
        }
    }
}
