package mutrivia.demo.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @JsonProperty("questionStatement")
    private String questionStatement;
    @JsonProperty("options")
    private List<String> options;
    @JsonProperty("correctChoiceIndex")
    private int correctChoiceIndex;
    @JsonProperty("userId")
    private String userId;
}
