package nl.tudelft.oopp.project.exceptions;

import nl.tudelft.oopp.project.entities.Session;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

public class SessionClosedException extends ResponseStatusException {
    public SessionClosedException(Session closedSession) {
        super(HttpStatus.GONE, "Session " + closedSession.getSessionName() + " is closed.");
    }
}
