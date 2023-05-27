package mutrivia.demo.backend.controller;

import mutrivia.demo.backend.model.TextData;
import mutrivia.demo.backend.rabbitmq.TextSender;
import mutrivia.demo.backend.service.QuestionService;
import mutrivia.demo.backend.service.TextDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="http://mutrivia.com")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private TextSender textSender;
    @Autowired
    private TextDataService textDataService;
    @Autowired
    private QuestionService questionService;
    @Value("${admin.id}")
    String adminId;
    @Value("${admin.password}")
    String adminPassword;

    public AdminController(TextSender textSender, TextDataService textDataService, QuestionService questionService) {
        this.textSender = textSender;
        this.textDataService = textDataService;
        this.questionService = questionService;
    }

    @GetMapping("/authenticate/{id}/{password}")
    public boolean authenticate(@PathVariable String id, @PathVariable String password){
        return id.equals(adminId) && password.equals(adminPassword);
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
