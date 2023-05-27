package mutrivia.demo.backend.controller;


import lombok.extern.log4j.Log4j;
import mutrivia.demo.backend.model.Session;
import mutrivia.demo.backend.model.User;
import mutrivia.demo.backend.model.constants.UserStateConstants;
import mutrivia.demo.backend.service.SessionService;
import mutrivia.demo.backend.service.UserService;
import mutrivia.demo.backend.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins="http://mutrivia.com")
@RestController
@RequestMapping("/api/user")
@Log4j
public class UserController {

    private UserService userService;
    private SessionService sessionService;
    private WebSocketService webSocketService;

    public UserController(@Autowired SessionService sessionService, UserService userService, WebSocketService webSocketService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.webSocketService = webSocketService;
    }

    @GetMapping("/session/{userId}")
    public List<User> getUsersInSession(@PathVariable String userId){
        String sessionId = userService.findUserById(userId).getSessionId();
        if(sessionId.equals("initial")){
            return null;
        }
        return userService.findUsersInSession(sessionId);
    }

    @GetMapping("/get/{userId}")
    public User getUser(@PathVariable String userId){
        return userService.findUserById(userId);
    }

    @PostMapping("/add/{username}")
    public User addUser(@PathVariable String username){
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setState(UserStateConstants.INITIAL_USER);
        newUser.setSessionId("initial");
        newUser.setScore(0);
        newUser = userService.addUser(newUser);
        return newUser;
    }

    @PutMapping("/addpoints/{userId}/{points}")
    public User addPointsToUser(@PathVariable String userId, @PathVariable int points){
        System.out.println("ADD POINT");
        User user = userService.findUserById(userId);
        long userScore = user.getScore();
        user.setScore(userScore+points);
        userService.updateUser(user);
        return user;
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Void> deleteAll(){
        userService.deleteAllUsers();
        return ResponseEntity.noContent().build();
    }

    /*
    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user){
        user.setPlaying(false);
        User addedUser = userService.addUser(user);
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }
    */

    /*
    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User user){
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
    */
}
