package nl.tudelft.oopp.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.tudelft.oopp.project.entities.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;


public class PollingEventsController {
    private static final Map<Long, List<DeferredResult<ResponseEntity<String>>>> listeners = new HashMap<>();

    /**
     * Add listeners to session events.
     * @param session session to which to listen
     * @param output deferred result response
     */
    public static void addListener(Session session, DeferredResult<ResponseEntity<String>> output) {
        if (!listeners.containsKey(session.getId())) {
            listeners.put(session.getId(), new ArrayList<>());
        }
        listeners.get(session.getId()).add(output);

    }

    /**
     * Emits event to all listeners.
     * @param session selects listeners
     * @param event string to transmit
     */
    public static void emit(Session session, String event) {
        List<DeferredResult<ResponseEntity<String>>> sessionListeners = listeners.get(session.getId());
        if (sessionListeners == null) {
            return;
        }
        for (DeferredResult<ResponseEntity<String>> l : sessionListeners) {
            if (!l.isSetOrExpired()) {
                l.setResult(ResponseEntity.ok(event));
            }
        }
        sessionListeners.clear();
    }

    public static Map<Long, List<DeferredResult<ResponseEntity<String>>>> getListeners() {
        return listeners;
    }
}
