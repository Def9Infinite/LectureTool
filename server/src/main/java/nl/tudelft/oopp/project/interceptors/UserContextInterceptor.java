package nl.tudelft.oopp.project.interceptors;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.tudelft.oopp.project.annotations.RequireUserContext;
import nl.tudelft.oopp.project.entities.User;
import nl.tudelft.oopp.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


public class UserContextInterceptor implements HandlerInterceptor {

    static double rate = 50; // unit: actions
    static double per = 3; // unit: seconds
    static double allowance = rate; // unit: actions

    @Autowired
    private UserRepository userRepository;

    public UserContextInterceptor(){

    }

    public UserContextInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequireUserContext requireUserContext = handlerMethod.getMethod().getAnnotation(RequireUserContext.class);
        if (requireUserContext == null) {
            return true; // Nothing needs to be done, controller method does not require user context.
        }

        String userTokenString = request.getHeader("X-USER-TOKEN");
        if (userTokenString == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // No token found
            return false;
        }

        User userContext = userRepository.findByToken(UUID.fromString(userTokenString));
        if (userContext == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // No user with this token found
            return false;
        }

        if (userContext.getSession().ipIsBanned(request.getRemoteAddr())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // User is banned.
        }

        double lastCheck = userContext.getLastActionTimestamp();
        userContext.setLastActionTimestamp(System.currentTimeMillis());
        double current = System.currentTimeMillis();
        double timePassed = (current - lastCheck) / 1000; // unit: seconds
        userContext.setLastActionTimestamp(current);
        allowance += timePassed * (rate / per);
        if (allowance > rate) {
            allowance = rate;
        }

        if (allowance < 1) {
            return false;
        } else {
            allowance -= 1;
        }
        userContext.setLastActionTimestamp(System.currentTimeMillis());
        userRepository.save(userContext);

        request.setAttribute("user_context", userContext);
        return true;
    }
}