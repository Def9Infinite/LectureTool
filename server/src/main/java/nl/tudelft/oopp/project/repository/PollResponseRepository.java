package nl.tudelft.oopp.project.repository;

import nl.tudelft.oopp.project.entities.Poll;
import nl.tudelft.oopp.project.entities.PollResponse;
import nl.tudelft.oopp.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollResponseRepository extends JpaRepository<PollResponse, Long> {
    PollResponse getPollResponseByUserAndPollOptionPoll(User user, Poll poll);
}
