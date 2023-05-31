package mutrivia.demo.backend.service;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import mutrivia.demo.backend.model.Question;
import mutrivia.demo.backend.model.Session;
import mutrivia.demo.backend.model.TextData;
import mutrivia.demo.backend.model.User;
import mutrivia.demo.backend.model.constants.UserStateConstants;
import mutrivia.demo.backend.rabbitmq.QuestionReceiver;
import mutrivia.demo.backend.rabbitmq.TextSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
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

    private Map<String, Integer> sessionIdPausedUserCountMap;
    private Map<String, Integer> sesssionIdAnweredUserCountMap;

    public GameService(UserService userService, SessionService sessionService, TextDataService textDataService, QuestionService questionService, TextSender textSender, QuestionReceiver questionReceiver, WebSocketService webSocketService, LeaderboardService leaderboardService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.textDataService = textDataService;
        this.questionService = questionService;
        this.textSender = textSender;
        this.questionReceiver = questionReceiver;
        this.webSocketService = webSocketService;
        this.leaderboardService = leaderboardService;
        this.sessionIdPausedUserCountMap = new HashMap<>();
        this.sesssionIdAnweredUserCountMap = new HashMap<>();
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
        List<User> usersInSession = userService.findUsersInSession(host.getSessionId());
        sesssionIdAnweredUserCountMap.put(host.getSessionId(), usersInSession.size());

        if(session.getTextDataIndex() >= textDataList.size()){
            Question endQuestion = new Question();
            endQuestion.setQuestionStatement("endsession");
            webSocketService.sendQuestionMessage(endQuestion, host.getUserId());
            if(usersInSession.size() == 1){
                return endQuestion;
            }
        }
        else {
            //int randomTextDataIndex = (int)(Math.random() * textDataList.size());
            String textDataIdToUse = textDataList.get(session.getTextDataIndex()).getTextDataId();
            List<Question> questionList = questionService.findQuestionByTextId(textDataIdToUse);
            if(usersInSession.size() == 1){
                int randomQuestionIndex = (int) (Math.random() * questionList.size());
                Question userQuestion = questionList.get(randomQuestionIndex);
                session.setTextDataIndex(session.getTextDataIndex()+1);
                sessionService.updateSession(session);
                return userQuestion;
            }
            for (User user : usersInSession) {
                int randomQuestionIndex = (int) (Math.random() * questionList.size());
                Question userQuestion = questionList.get(randomQuestionIndex);
                webSocketService.sendQuestionMessage(userQuestion, user.getUserId());
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
        List<User> usersInSession = userService.findUsersInSession(session.getSessionId());
        sessionIdPausedUserCountMap.put(session.getSessionId(), usersInSession.size());
        sesssionIdAnweredUserCountMap.put(session.getSessionId(), usersInSession.size());
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
        sessionIdPausedUserCountMap.remove(user.getSessionId());
        sesssionIdAnweredUserCountMap.remove(user.getSessionId());
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
        if(sessionIdPausedUserCountMap.get(user.getSessionId()) != null){
            int currentUserCount = sessionIdPausedUserCountMap.get(user.getSessionId());
            sessionIdPausedUserCountMap.put(user.getSessionId(), currentUserCount-1);
            sesssionIdAnweredUserCountMap.put(user.getSessionId(), currentUserCount-1);
        }
        user.setState(UserStateConstants.INITIAL_USER);
        user.setSessionId("initial");
        user.setScore(0);
        userService.updateUser(user);
    }

    public void updatePauseMap(String sessionId){
        if(sessionIdPausedUserCountMap.containsKey(sessionId)){
            int currentUserCount = sessionIdPausedUserCountMap.get(sessionId);
            sessionIdPausedUserCountMap.put(sessionId, currentUserCount-1);
        }
    }

    public int checkPauseMap(String sessionId){
        if(sessionIdPausedUserCountMap.containsKey(sessionId)){
            if(sessionIdPausedUserCountMap.get(sessionId)==1){
                List<User> usersInSession = userService.findUsersInSession(sessionId);
                sessionIdPausedUserCountMap.put(sessionId, usersInSession.size());
                return 1;
            }
            else{
                return 2;
            }
        }
        return 3;
    }

    public void answerQuestion(String userId){
        User user = userService.findUserById(userId);
        int usersLeftToAnswerCount = sesssionIdAnweredUserCountMap.get(user.getSessionId()) - 1;
        System.out.println("USERS LEFT TO ANSWER: " + usersLeftToAnswerCount);
        if(usersLeftToAnswerCount == 0){
            System.out.println("Change Question");
            List<User> usersInSession = userService.findUsersInSession(user.getSessionId());
            sesssionIdAnweredUserCountMap.put(user.getSessionId(), usersInSession.size());
            webSocketService.changeSessionQuestion(user.getSessionId());
        }
        else{
            System.out.println("A user answered");
            sesssionIdAnweredUserCountMap.put(user.getSessionId(), usersLeftToAnswerCount);
        }
    }

    public int getSessionQuestionLength(String museumId){
        List<TextData> textDataList = textDataService.findTextDataByMuseumId(museumId);
        return textDataList.size();
    }
}
