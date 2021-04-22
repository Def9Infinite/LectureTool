package nl.tudelft.oopp.project.repository;

import java.util.List;

import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.SpeedFeedback;
import nl.tudelft.oopp.project.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SpeedFeedbackRepository extends JpaRepository<SpeedFeedback, Long> {
    @Query("select sf from SpeedFeedback sf where sf.session = ?1  and sf.user = ?2")
    SpeedFeedback getByUserAndSession(Session session, User user);

    @Query(value = "SELECT sf_c from generate_series(0,4) as t(i) "
            + "LEFT JOIN ("
            + "   SELECT speed_feedback as sf, count(*) as sf_c FROM speed_feedback "
            + "   WHERE session_id = ?1 GROUP BY sf "
            + ") sf ON sf.sf = i",
            nativeQuery = true)
    List<Integer> listLectureSpeedsForSession(long sessionId);

}
