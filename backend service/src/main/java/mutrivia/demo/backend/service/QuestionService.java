package mutrivia.demo.backend.service;

import mutrivia.demo.backend.model.Question;
import mutrivia.demo.backend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> findQuestionByText(Question question){
        return questionRepository.findQuestionByQuestionStatement(question.getQuestionStatement());
    }
    public List<Question> findQuestionByTextId(String textDataId){
        return questionRepository.findQuestionByTextDataId(textDataId);
    }

    public void addQuestion(Question question){
        questionRepository.save(question);
    }

    public void deleteQuestion(String questionId){
        questionRepository.deleteById(questionId);
    }
}
