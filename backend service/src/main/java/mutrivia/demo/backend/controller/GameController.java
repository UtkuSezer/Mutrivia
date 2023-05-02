package mutrivia.demo.backend.controller;

import mutrivia.demo.backend.model.LeaderboardRecord;
import mutrivia.demo.backend.model.Question;
import mutrivia.demo.backend.model.User;
import mutrivia.demo.backend.rabbitmq.QuestionReceiver;
import mutrivia.demo.backend.rabbitmq.TextSender;
import mutrivia.demo.backend.service.GameService;
import mutrivia.demo.backend.service.LeaderboardService;
import mutrivia.demo.backend.service.UserService;
import mutrivia.demo.backend.service.WebSocketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    public GameController(GameService gameService, UserService userService, WebSocketService webSocketService, LeaderboardService leaderboardService, TextSender textSender, QuestionReceiver questionReceiver) {
        this.gameService = gameService;
        this.userService = userService;
        this.webSocketService = webSocketService;
        this.leaderboardService = leaderboardService;
        this.textSender = textSender;
        this.questionReceiver = questionReceiver;
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

    @PutMapping("/join/{sessionId}/{userId}")
    public User joinSession(@PathVariable String sessionId, @PathVariable String userId){
        User participant = gameService.joinSession(sessionId, userId);
        webSocketService.sendNewUserMessage(participant, participant.getSessionId());
        return participant;
    }

    @PutMapping("/host/{userId}/{museumId}")
    public User hostSession(@PathVariable String userId, @PathVariable String museumId){
        return gameService.hostSession(userId, museumId);
    }

    @PutMapping("/solo/{userId}/{museumId}")
    public User soloSession(@PathVariable String userId, @PathVariable String museumId){
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
