package mutrivia.demo.backend.service;

import mutrivia.demo.backend.model.User;
import mutrivia.demo.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findUsersInSession(String sessionId){
        return userRepository.findBySessionId(sessionId);
    }

    public List<User> findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User findUserById(String id){
        return userRepository.findById(id).get();
    }

    public User addUser(User user){
        return userRepository.save(user);
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }

    public void deleteAllUsers(){
        userRepository.deleteAll();
    }

}
