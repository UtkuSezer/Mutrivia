package mutrivia.demo.backend.repository;

import mutrivia.demo.backend.model.TextData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextDataRepository extends CrudRepository<TextData, String> {
    List<TextData> findByMuseumId(String museumId);
    List<TextData> findByText(String text);
}
