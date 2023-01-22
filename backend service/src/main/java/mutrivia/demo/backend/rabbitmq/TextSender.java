package mutrivia.demo.backend.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class TextSender {

    @Autowired
    private RabbitTemplate template;

    public void send(String text, String userId){
        this.template.convertAndSend("text", text + "#*$*#" + userId);
    }
}
