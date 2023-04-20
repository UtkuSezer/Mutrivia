package mutrivia.demo.backend.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mutrivia.demo.backend.model.Question;
import mutrivia.demo.backend.service.QuestionService;
import mutrivia.demo.backend.service.WebSocketService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class QuestionReceiver {

    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private QuestionService questionService;

    public QuestionReceiver(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RabbitListener(queues = "question")
    public void receive(Question message) {
        System.out.println("Received. Saving question with text data ID: " + message.getTextDataId());

        boolean flag = true;

        Question question = new Question();
        question.setQuestionStatement(message.getQuestionStatement());
        question.setOptions(message.getOptions());
        question.setTextDataId(message.getTextDataId());
        question.setCorrectChoiceIndex(message.getCorrectChoiceIndex());

        List<Question> questionListWithSameTextId = questionService.findQuestionByTextId(message.getTextDataId());
        if(questionListWithSameTextId.size() == 0){
            questionService.addQuestion(question);
        }
        else{
            for(Question existingQuestion : questionListWithSameTextId){
                if(existingQuestion.getQuestionStatement().equals(question.getQuestionStatement())){
                    flag = false;
                }
            }
            if(flag){
                questionService.addQuestion(question);
            }
        }
        //webSocketService.sendQuestionMessage(message);
    }
}
