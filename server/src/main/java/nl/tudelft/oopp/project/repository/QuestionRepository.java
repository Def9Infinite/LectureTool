package nl.tudelft.oopp.project.repository;

import java.util.List;
import nl.tudelft.oopp.project.entities.Question;
import nl.tudelft.oopp.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface QuestionRepository extends JpaRepository<Question,Long> {
    Question findBysessionId(long sessionId);

    List<Question> findAllBySessionId(long sessionId);

    List<Question> findAllBySessionIdAndUserId(long sessionId, long userId);

    @Query(value = "SELECT "
            + "*, (GREATEST(0, upvote_count) + EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - created_at))/60) score "
            + "FROM question "
            + "LEFT JOIN (SELECT question_id, COUNT(*) as upvote_count FROM upvote GROUP BY question_id)"
            + "upvote ON question.id = upvote.question_id "
            + "WHERE question.session_id = ?1 AND question.answer_status = FALSE "
            + "ORDER BY score DESC",
            nativeQuery = true
    )
    List<Question> getRankedQuestionsBySessionId(long sessionId);

    @Query(value = "SELECT "
            + "*, (GREATEST(0, upvote_count) + EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - created_at))/60) score "
            + "FROM question "
            + "LEFT JOIN (SELECT question_id, COUNT(*) as upvote_count FROM upvote GROUP BY question_id)"
            + "upvote ON question.id = upvote.question_id "
            + "WHERE question.session_id = ?1 AND question.answer_status = TRUE "
            + "ORDER BY score DESC",
            nativeQuery = true
    )

    List<Question> getRankedAnsweredQuestionsBySessionId(long sessionId);

    List<Question> findAllBySessionIdAndAnswerStatus(long sessionId, boolean status);

    void deleteAllByUser(User user);

}
