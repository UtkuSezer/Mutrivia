package mutrivia.demo.backend.repository;

import mutrivia.demo.backend.model.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends CrudRepository<Question, String> {
    List<Question> findQuestionByQuestionStatement(String text);
    List<Question> findQuestionByTextDataId(String textDataId);
}
