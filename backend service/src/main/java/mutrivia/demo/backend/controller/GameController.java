package mutrivia.demo.backend.controller;

import mutrivia.demo.backend.model.LeaderboardRecord;
import mutrivia.demo.backend.model.Question;
import mutrivia.demo.backend.model.User;
import mutrivia.demo.backend.rabbitmq.QuestionReceiver;
import mutrivia.demo.backend.rabbitmq.TextSender;
import mutrivia.demo.backend.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/api/game")
public class GameController {

    private GameService gameService;
    private UserService userService;
    private WebSocketService webSocketService;
    private LeaderboardService leaderboardService;
    private TextSender textSender;
    private QuestionReceiver questionReceiver;
    private TextDataService textDataService;

    public GameController(GameService gameService, UserService userService, WebSocketService webSocketService, LeaderboardService leaderboardService, TextSender textSender, QuestionReceiver questionReceiver, TextDataService textDataService) {
        this.gameService = gameService;
        this.userService = userService;
        this.webSocketService = webSocketService;
        this.leaderboardService = leaderboardService;
        this.textSender = textSender;
        this.questionReceiver = questionReceiver;
        this.textDataService = textDataService;
    }

    @GetMapping("/question/{userId}")
    public Question nextQuestion(@PathVariable String userId){
        return gameService.changeSessionQuestion(userId);
    }

    @GetMapping("/start/{userId}")
    public void startSession(@PathVariable String userId){
        gameService.startSession(userId);
        User user = userService.findUserById(userId);
        webSocketService.sendStartSessionMessage(user.getSessionId());
    }

    @GetMapping("/notifypause/{sessionId}")
    public void notifyPause(@PathVariable String sessionId){
        gameService.updatePauseMap(sessionId);
    }

    @GetMapping("/checkpause/{sessionId}")
    public boolean checkPause(@PathVariable String sessionId){
        return gameService.checkPauseMap(sessionId);
    }

    @GetMapping("/answerquestion/{userId}")
    public void questionAnswered(@PathVariable String userId){
        gameService.answerQuestion(userId);
    }

    @PutMapping("/join/{sessionId}/{userId}")
    public User joinSession(@PathVariable String sessionId, @PathVariable String userId) throws InterruptedException {
        User participant;
        try {
            participant = gameService.joinSession(sessionId, userId);
        } catch ( NoSuchElementException e) {
            return null;
        }
        Thread.sleep(1000);
        webSocketService.sendNewUserMessage(participant, participant.getSessionId());
        return participant;
    }

    @PutMapping("/host/{userId}/{museumId}")
    public User hostSession(@PathVariable String userId, @PathVariable String museumId){
        if(textDataService.findTextDataByMuseumId(museumId).size() == 0){
            return null;
        }
        return gameService.hostSession(userId, museumId);
    }

    @PutMapping("/solo/{userId}/{museumId}")
    public User soloSession(@PathVariable String userId, @PathVariable String museumId){
        if(textDataService.findTextDataByMuseumId(museumId).size() == 0){
            return null;
        }
        return gameService.startSoloSession(userId, museumId);
    }

    @GetMapping("/leave/{userId}")
    public void leaveSession(@PathVariable String userId){
        User user = userService.findUserById(userId);
        webSocketService.sendDeleteUserMessage(userId, user.getSessionId());
        gameService.leaveSession(user);
    }

    @GetMapping("/results/{userId}")
    public void switchToResults(@PathVariable String userId){
        User user = userService.findUserById(userId);
        webSocketService.sendDeleteSessionMessage(user.getSessionId());
    }

    @GetMapping("/checkleaderboard/{userId}")
    public void checkLeaderboard(@PathVariable String userId){
        User user = userService.findUserById(userId);
        leaderboardService.checkIfHighScore(user);
    }

    @GetMapping("/leaderboard/{userId}")
    public List<LeaderboardRecord> getLeaderboard(@PathVariable String userId){
        User user = userService.findUserById(userId);
        return leaderboardService.getLeaderboardRecords(user.getMuseumId());
    }

    @GetMapping("/end/{userId}")
    public void endSession(@PathVariable String userId){
        User user = userService.findUserById(userId);
        //webSocketService.sendDeleteSessionMessage(user.getSessionId());
        gameService.endSession(userId);
    }

    @DeleteMapping("/delete/all/leaderboard/{museumId}")
    public ResponseEntity<Void> deleteAllLeaderboard(@PathVariable String museumId){
        leaderboardService.deleteAll(leaderboardService.getLeaderboardRecords(museumId));
        return ResponseEntity.noContent().build();
    }
}
