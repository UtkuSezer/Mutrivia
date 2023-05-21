package mutrivia.demo.backend.service;

import mutrivia.demo.backend.model.Question;
import mutrivia.demo.backend.model.Session;
import mutrivia.demo.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNewUserMessage(User user, String sessionId) {
        this.messagingTemplate.convertAndSend("/topic/newuser/" + sessionId, user);
    }

    public void sendDeleteUserMessage(String userId, String sessionId) {
        this.messagingTemplate.convertAndSend("/topic/deleteuser/" + sessionId, userId);
    }

    public void sendQuestionMessage(Question question, String userId) {
        this.messagingTemplate.convertAndSend("/topic/question/" + userId, question);
    }

    public void sendStartSessionMessage(String sessionId) {
        this.messagingTemplate.convertAndSend("/topic/startsession/" + sessionId, sessionId);
    }

    public void sendDeleteSessionMessage(String sessionId) {
        this.messagingTemplate.convertAndSend("/topic/deletesession/" + sessionId, sessionId);
    }

    public void changeSessionQuestion(String sessionId) {
        this.messagingTemplate.convertAndSend("/topic/changequestion/" + sessionId, sessionId);
    }
}
