package mutrivia.demo.backend.controller;

import mutrivia.demo.backend.jwt.JwtRequestModel;
import mutrivia.demo.backend.jwt.JwtResponseModel;
import mutrivia.demo.backend.jwt.JwtUserDetailsService;
import mutrivia.demo.backend.jwt.TokenManager;
import mutrivia.demo.backend.model.TextData;
import mutrivia.demo.backend.rabbitmq.TextSender;
import mutrivia.demo.backend.service.QuestionService;
import mutrivia.demo.backend.service.TextDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TextSender textSender;
    @Autowired
    private TextDataService textDataService;
    @Autowired
    private QuestionService questionService;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private TokenManager tokenManager;

    /*
    @Value("${admin.id}")
    String adminId;
    @Value("${admin.password}")
    String adminPassword;
    */

    public AdminController(TextSender textSender, TextDataService textDataService, QuestionService questionService) {
        this.textSender = textSender;
        this.textDataService = textDataService;
        this.questionService = questionService;
    }

    @GetMapping("/authenticate")
    public ResponseEntity authenticate(@RequestBody JwtRequestModel request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new
                            UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword())
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwtToken = tokenManager.generateJwtToken(userDetails);
        return ResponseEntity.ok(new JwtResponseModel(jwtToken));

        //return id.equals(adminId) && password.equals(adminPassword);
    }

    @GetMapping("/generate/{museumId}/{artifactText}")
    public void generateQuestion(@PathVariable String museumId, @PathVariable String artifactText){
        TextData textData = new TextData();
        textData.setMuseumId(museumId);
        textData.setText(artifactText);

        textData = textDataService.addTextData(textData);

        textSender.send(artifactText, textData.getTextDataId());
    }

    @DeleteMapping("/deletequestion/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable String questionId){
        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteallquestions")
    public ResponseEntity<Void> deleteQuestion(){
        questionService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deletetextdatabyid/{textDataId}")
    public ResponseEntity<Void> deleteTextDataById(@PathVariable String textDataId){
        textDataService.deleteTextDataById(textDataId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deletetextdatabymuseum/{museumId}")
    public ResponseEntity<Void> deleteTextDataByMuseumId(@PathVariable String museumId){
        textDataService.deleteTextDataByMuseumId(museumId);
        return ResponseEntity.noContent().build();
    }

}
