package nl.tudelft.oopp.project.repository;

import java.util.List;
import nl.tudelft.oopp.project.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;



public interface EventRepository extends JpaRepository<Event,Long> {

    List<Event> findAllBySessionId(long id);
}
