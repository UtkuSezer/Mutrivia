package mutrivia.demo.backend.controller;

import mutrivia.demo.backend.model.Question;
import mutrivia.demo.backend.model.User;
import mutrivia.demo.backend.rabbitmq.QuestionReceiver;
import mutrivia.demo.backend.rabbitmq.TextSender;
import mutrivia.demo.backend.service.GameService;
import mutrivia.demo.backend.service.UserService;
import mutrivia.demo.backend.service.WebSocketService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/game")
public class GameController {

    private GameService gameService;
    private UserService userService;
    private WebSocketService webSocketService;
    private TextSender textSender;
    private QuestionReceiver questionReceiver;

    public GameController(GameService gameService, UserService userService, WebSocketService webSocketService, TextSender textSender, QuestionReceiver questionReceiver) {
        this.gameService = gameService;
        this.userService = userService;
        this.webSocketService = webSocketService;
        this.textSender = textSender;
        this.questionReceiver = questionReceiver;
    }

    @GetMapping("/question/{userId}")
    public void nextQuestion(@PathVariable String userId){
        gameService.changeSessionQuestion(userId);
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

    @GetMapping("/end/{userId}")
    public void endSession(@PathVariable String userId){
        User user = userService.findUserById(userId);
        //webSocketService.sendDeleteSessionMessage(user.getSessionId());
        gameService.endSession(userId);
    }
}
