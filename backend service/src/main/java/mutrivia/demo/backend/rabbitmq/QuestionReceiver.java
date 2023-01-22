package mutrivia.demo.backend.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mutrivia.demo.backend.model.Question;
import mutrivia.demo.backend.service.WebSocketService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class QuestionReceiver {

    @Autowired
    private WebSocketService webSocketService;

    @RabbitListener(queues = "question")
    public void receive(Question message) {
        System.out.println("Received: " + message.getUserId());
        webSocketService.sendQuestionMessage(message);
    }
}
