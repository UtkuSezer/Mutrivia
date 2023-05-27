package mutrivia.demo.backend.controller;

import mutrivia.demo.backend.model.Session;
import mutrivia.demo.backend.model.User;
import mutrivia.demo.backend.service.SessionService;
import mutrivia.demo.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="http://mutrivia.com")
@RestController
@RequestMapping("/api/session")
public class SessionController {

    private SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/get/{sessionId}")
    public Session getSession(@PathVariable String sessionId){
        return sessionService.findSessionById(sessionId);
    }

    @PostMapping("/add/{userId}")
    public void addSession(@PathVariable String userId){
        sessionService.addSession(userId);
    }

    @DeleteMapping("/delete/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable String sessionId){
        sessionService.deleteSession(sessionId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Void> deleteAll(){
        sessionService.deleteAllSessions();
        return ResponseEntity.noContent().build();
    }
}
