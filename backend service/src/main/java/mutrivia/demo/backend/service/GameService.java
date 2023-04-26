package mutrivia.demo.backend.service;

import lombok.extern.log4j.Log4j;
import mutrivia.demo.backend.model.Question;
import mutrivia.demo.backend.model.Session;
import mutrivia.demo.backend.model.TextData;
import mutrivia.demo.backend.model.User;
import mutrivia.demo.backend.model.constants.UserStateConstants;
import mutrivia.demo.backend.rabbitmq.QuestionReceiver;
import mutrivia.demo.backend.rabbitmq.TextSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j
public class GameService {

    private UserService userService;
    private SessionService sessionService;
    private TextDataService textDataService;
    private QuestionService questionService;
    private TextSender textSender;
    private QuestionReceiver questionReceiver;
    private WebSocketService webSocketService;
    private LeaderboardService leaderboardService;

    public GameService(UserService userService, SessionService sessionService, TextDataService textDataService, QuestionService questionService, TextSender textSender, QuestionReceiver questionReceiver, WebSocketService webSocketService, LeaderboardService leaderboardService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.textDataService = textDataService;
        this.questionService = questionService;
        this.textSender = textSender;
        this.questionReceiver = questionReceiver;
        this.webSocketService = webSocketService;
        this.leaderboardService = leaderboardService;
    }

    public void generateQuestion(String userId){
        User host = userService.findUserById(userId);
        //TODO: Upload text data to database
        //List<TextData> textDataList = textDataService.findTextDataByMuseumId(host.getMuseumId());
        //int randomIndex = (int)(Math.random() * textDataList.size());
        //String textToUse = textDataList.get(randomIndex).getText();
        String textToUse = "This three-bolt diving helmet and suit were made in Russia in 1983. The basic design of " +
                "the helmet is similar to the British Siebe-Gorman 12-bolt from the 1840s, and is still in production " +
                "(for collectors) today! It is designed to be used with compressed air, not the more sophisticated" +
                " mixed-gas technology used for deeper diving";
        List<User> usersInSession = userService.findUsersInSession(host.getSessionId());
        for(User user: usersInSession){
            //textSender.send(textToUse, user.getUserId());
        }
    }

    public Question changeSessionQuestion(String userId){
        User host = userService.findUserById(userId);
        Session session = sessionService.findSessionByHostId(host.getUserId());
        List<TextData> textDataList = textDataService.findTextDataByMuseumId(host.getMuseumId());

        if(session.getTextDataIndex() >= textDataList.size()){
            Question endQuestion = new Question();
            endQuestion.setQuestionStatement("endsession");
            webSocketService.sendQuestionMessage(endQuestion, host.getUserId());
        }
        else {
            //int randomTextDataIndex = (int)(Math.random() * textDataList.size());
            String textDataIdToUse = textDataList.get(session.getTextDataIndex()).getTextDataId();
            List<Question> questionList = questionService.findQuestionByTextId(textDataIdToUse);

            List<User> usersInSession = userService.findUsersInSession(host.getSessionId());
            for (User user : usersInSession) {
                int randomQuestionIndex = (int) (Math.random() * questionList.size());
                Question userQuestion = questionList.get(randomQuestionIndex);
                webSocketService.sendQuestionMessage(userQuestion, user.getUserId());
                System.out.println("Send Question");
                return userQuestion;
            }
            session.setTextDataIndex(session.getTextDataIndex()+1);
            sessionService.updateSession(session);
        }
        return null;
    }

    public void startSession(String userId){
        User host = userService.findUserById(userId);
        Session session = sessionService.findSessionByHostId(userId);
        session.setJoinable(false);
        sessionService.updateSession(session);
    }

    public User joinSession(String sessionId, String userId){
        Session sessionToJoin = sessionService.findSessionById(sessionId);
        if(sessionToJoin == null){
            log.error("Session with id: " + sessionId + " does not exists.");
        }
        else {
            if(sessionToJoin.isJoinable()){
                User participantUser = userService.findUserById(userId);
                participantUser.setSessionId(sessionId);
                participantUser.setMuseumId(userService.findUserById(sessionToJoin.getHostId()).getMuseumId());
                participantUser.setState(UserStateConstants.PARTICIPANT_USER);
                userService.updateUser(participantUser);
                return participantUser;
            }
            else{
                log.error("Session with id: " + sessionId + " is not currently joinable.");
            }
        }
        return null;
    }

    public User hostSession(String userId, String museumId){
        sessionService.addSession(userId);
        User hostUser = userService.findUserById(userId);
        hostUser.setSessionId(sessionService.findSessionByHostId(userId).getSessionId());
        hostUser.setState(UserStateConstants.HOST_USER);
        hostUser.setMuseumId(museumId);
        userService.updateUser(hostUser);
        return hostUser;
    }

    public User startSoloSession(String userId, String museumId){
        sessionService.addSoloSession(userId);
        User soloUser = userService.findUserById(userId);
        soloUser.setSessionId(sessionService.findSessionByHostId(userId).getSessionId());
        soloUser.setMuseumId(museumId);
        soloUser.setState(UserStateConstants.SOLO_USER);
        userService.updateUser(soloUser);
        return soloUser;
    }

    public void endSession(String userId){
        User user = userService.findUserById(userId);
        if(user.getState().equals(UserStateConstants.HOST_USER)){
            String winner;
            int max = Integer.MIN_VALUE;
            List<User> sessionUserList = userService.findUsersInSession(user.getSessionId());
            sessionService.deleteSession(user.getSessionId());
            for(User participant: sessionUserList){
                if(max < participant.getScore()){
                    winner = participant.getUsername();
                }
                leaveSession(participant);
            }
            leaveSession(user);
        }
        else if(user.getState().equals(UserStateConstants.SOLO_USER)){
            sessionService.deleteSession(user.getSessionId());
            leaveSession(user);
        }
        else{
            log.error("User with id: " + userId + "is not authorized to end the session.");
        }
    }

    public void leaveSession(User user){
        user.setState(UserStateConstants.INITIAL_USER);
        user.setSessionId("initial");
        user.setScore(0);
        userService.updateUser(user);
    }
}
