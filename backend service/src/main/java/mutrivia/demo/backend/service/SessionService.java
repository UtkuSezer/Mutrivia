package mutrivia.demo.backend.service;

import mutrivia.demo.backend.model.Session;
import mutrivia.demo.backend.repository.SessionRepository;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    private SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session findSessionById(String sessionId){
        return sessionRepository.findById(sessionId).get();
    }

    public Session findSessionByHostId(String hostId){
        return sessionRepository.findByHostId(hostId);
    }

    public Session addSession(String userId){
        Session session = new Session();
        session.setHostId(userId);
        session.setJoinable(true);
        session.setTextDataIndex(0);
        return sessionRepository.save(session);
    }

    public Session addSoloSession(String userId){
        Session session = new Session();
        session.setHostId(userId);
        session.setJoinable(false);
        session.setTextDataIndex(0);
        return sessionRepository.save(session);
    }

    public Session updateSession(Session session){
        return sessionRepository.save(session);
    }
    public void deleteSession(String sessionId){
        sessionRepository.deleteById(sessionId);
    }

    public void deleteAllSessions(){
        sessionRepository.deleteAll();
    }
}
