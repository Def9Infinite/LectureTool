package nl.tudelft.oopp.project.repository;

import java.util.List;

import nl.tudelft.oopp.project.entities.Poll;
import nl.tudelft.oopp.project.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PollRepository extends JpaRepository<Poll,Long> {
    List<Poll> findAllBySessionOrderByIdDesc(Session session);
}
