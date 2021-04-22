package nl.tudelft.oopp.project.exceptions;

import nl.tudelft.oopp.project.entities.Session;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IpBannedException extends ResponseStatusException {
    public IpBannedException(Session session) {
        super(HttpStatus.FORBIDDEN, "Your IP is blocked from attending " + session.getSessionName() + ".");
    }
}
