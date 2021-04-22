package nl.tudelft.oopp.project.repository;

import java.util.UUID;

import nl.tudelft.oopp.project.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


public interface UserRepository extends JpaRepository<User,Long> {
    User findByToken(UUID token);
}
