package mutrivia.demo.backend.service;

import mutrivia.demo.backend.model.Session;
import mutrivia.demo.backend.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session findSessionById(String sessionId){
        return sessionRepository.findById(sessionId).get();
    }

    public Session findSessionByHostId(String hostId){
        return sessionRepository.findByHostId(hostId);
    }

    public void addSession(String userId){
        Session session = new Session();
        session.setSessionId(generateSessionId());
        session.setHostId(userId);
        session.setJoinable(true);
        session.setTextDataIndex(0);
        sessionRepository.save(session);
    }

    public void addSoloSession(String userId){
        Session session = new Session();
        session.setSessionId(generateSessionId());
        session.setHostId(userId);
        session.setJoinable(false);
        session.setTextDataIndex(0);
        sessionRepository.save(session);
    }

    public String generateSessionId(){
        String newSessionId = "";
        do{
            Random rand = new Random();
            newSessionId = Integer.toString(rand.nextInt(1000000 - 100000 + 1) + 100000);
        } while(sessionRepository.findById(newSessionId).isPresent());

        return newSessionId;
    }

    public void updateSession(Session session){
        sessionRepository.save(session);
    }
    public void deleteSession(String sessionId){
        sessionRepository.deleteById(sessionId);
    }

    public void deleteAllSessions(){
        sessionRepository.deleteAll();
    }
}
