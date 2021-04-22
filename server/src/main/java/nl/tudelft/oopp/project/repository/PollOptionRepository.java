package nl.tudelft.oopp.project.repository;

import nl.tudelft.oopp.project.entities.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollOptionRepository extends JpaRepository<PollOption,Long> {

}
