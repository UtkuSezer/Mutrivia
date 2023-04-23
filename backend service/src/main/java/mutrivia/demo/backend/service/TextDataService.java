package mutrivia.demo.backend.service;

import mutrivia.demo.backend.model.TextData;
import mutrivia.demo.backend.repository.TextDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TextDataService {
    private TextDataRepository textDataRepository;

    public TextDataService(TextDataRepository textDataRepository) {
        this.textDataRepository = textDataRepository;
    }

    public List<TextData> findTextDataByMuseumId(String museumId){
        return textDataRepository.findByMuseumId(museumId);
    }

    public TextData addTextData(TextData textData){
        List<TextData> textDataList = textDataRepository.findByText(textData.getText());
        if(textDataList.size()>0){
            for(TextData textDataIteration: textDataList){
                if(textDataIteration.getMuseumId().equals(textData.getMuseumId())){
                    return null;
                }
            }
        }
        return textDataRepository.save(textData);
    }

    public void deleteTextDataById(String textDataId){
        textDataRepository.deleteById(textDataId);
    }

    public void deleteTextDataByMuseumId(String museumId){
        List<TextData> textDataList = findTextDataByMuseumId(museumId);
        for(TextData textData: textDataList){
            textDataRepository.deleteById(textData.getTextDataId());
        }
    }
}
