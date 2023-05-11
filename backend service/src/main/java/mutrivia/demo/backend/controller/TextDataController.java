package mutrivia.demo.backend.controller;

import mutrivia.demo.backend.model.TextData;
import mutrivia.demo.backend.service.TextDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/textdata")
public class TextDataController {
    private TextDataService textDataService;

    public TextDataController(TextDataService textDataService) {
        this.textDataService = textDataService;
    }

    @PostMapping("/add")
    public ResponseEntity<TextData> addTextData(@RequestBody TextData textData){
        TextData newTextData = textDataService.addTextData(textData);
        return new ResponseEntity<TextData>(textData, HttpStatus.CREATED);
    }
}
