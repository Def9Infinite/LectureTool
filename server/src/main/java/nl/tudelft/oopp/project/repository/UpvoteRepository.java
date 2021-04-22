package nl.tudelft.oopp.project.repository;

import java.util.List;

import nl.tudelft.oopp.project.entities.Question;
import nl.tudelft.oopp.project.entities.Upvote;
import nl.tudelft.oopp.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UpvoteRepository extends JpaRepository<Upvote, Long> {
    List<Upvote> findAllByQuestionId(long questionId);

    @Query("select u from Upvote u where u.question = ?1 and u.user = ?2")
    Upvote findUpvotesByUserAndQuestion(Question question, User user);

    Void removeAllByUser(User user);

    void removeAllByQuestion(Question question);
}
