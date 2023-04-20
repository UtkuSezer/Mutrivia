package mutrivia.demo.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String questionId;
    @JsonProperty("questionStatement")
    private String questionStatement;
    @JsonProperty("options")
    private List<String> options;
    @JsonProperty("correctChoiceIndex")
    private int correctChoiceIndex;
    @JsonProperty("textDataId")
    private String textDataId;
}
