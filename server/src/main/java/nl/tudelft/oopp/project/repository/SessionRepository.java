package nl.tudelft.oopp.project.repository;

import java.util.UUID;

import nl.tudelft.oopp.project.entities.Session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SessionRepository extends JpaRepository<Session,Long> {

    Session findBypowerToken(UUID token);

    Session findBystudentToken(UUID token);

    @Query("select s from Session s where s.powerToken = ?1 or s.studentToken = ?1")
    Session findByAnyToken(UUID token);

    Session findById(long id);
}
