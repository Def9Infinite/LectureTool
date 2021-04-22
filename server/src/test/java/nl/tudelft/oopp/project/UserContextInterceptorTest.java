package nl.tudelft.oopp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;

import nl.tudelft.oopp.project.annotations.RequireUserContext;
import nl.tudelft.oopp.project.entities.Session;
import nl.tudelft.oopp.project.entities.User;
import nl.tudelft.oopp.project.interceptors.UserContextInterceptor;

import nl.tudelft.oopp.project.repository.SessionRepository;
import nl.tudelft.oopp.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

@DataJpaTest
public class UserContextInterceptorTest {
    private static class TestController {
        @RequireUserContext
        public void annotatedTestMethod() {
        }

        public void notAnnotatedTestMethod() {
        }
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionRepository sessionRepository;

    User user;

    /**
     * Set up test data.
     */
    @BeforeEach
    public void createTestData() {
        UUID powerToken = UUID.randomUUID();
        UUID studentToken = UUID.randomUUID();
        String sessionName = "session";
        Session session = new Session(powerToken, studentToken, sessionName);
        sessionRepository.save(session);

        UUID userToken = UUID.randomUUID();
        int role = 1;
        String nickname = "user1";
        String ipAddress = "130.161.128.82";
        user = new User(userToken, role, nickname, ipAddress, session);
        userRepository.save(user);
    }

    @Test
    public void notAnnotatedTest() throws Exception {
        Method method = TestController.class.getMethod("notAnnotatedTestMethod");
        TestController controller = new TestController();
        MockHttpServletRequest request = new MockHttpServletRequest("PUT","/testURL/");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);

        UserContextInterceptor interceptor = new UserContextInterceptor();
        boolean interceptorResult = interceptor.preHandle(request, response, handlerMethod);

        assertTrue(interceptorResult); // Check if request continues
        assertEquals(response.getStatus(), HttpServletResponse.SC_OK); // Check if status is 200
    }

    @Test
    public void annotatedButNoTokenHeaderTest() throws Exception {
        Method method = TestController.class.getMethod("annotatedTestMethod");
        TestController controller = new TestController();
        MockHttpServletRequest request = new MockHttpServletRequest("GET","/testURL/");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);

        UserContextInterceptor interceptor = new UserContextInterceptor();
        boolean interceptorResult = interceptor.preHandle(request, response, handlerMethod);

        assertFalse(interceptorResult); // Check if request gets terminated
        assertEquals(response.getStatus(), HttpServletResponse.SC_UNAUTHORIZED); // Check if status is 401
    }

    @Test
    public void annotatedButIncorrectTokenHeaderTest() throws Exception {
        Method method = TestController.class.getMethod("annotatedTestMethod");
        TestController controller = new TestController();
        MockHttpServletRequest request = new MockHttpServletRequest("POST","/testURL/");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);

        request.addHeader("X-USER-TOKEN", UUID.randomUUID().toString());

        UserContextInterceptor interceptor = new UserContextInterceptor(userRepository);
        boolean interceptorResult = interceptor.preHandle(request, response, handlerMethod);

        assertFalse(interceptorResult); // Check if request gets terminated
        assertEquals(response.getStatus(), HttpServletResponse.SC_UNAUTHORIZED); // Check if status is 401
    }

    @Test
    public void annotatedWithCorrectTokenHeaderTest() throws Exception {
        Method method = TestController.class.getMethod("annotatedTestMethod");
        TestController controller = new TestController();
        MockHttpServletRequest request = new MockHttpServletRequest("POST","/testURL/");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);

        request.addHeader("X-USER-TOKEN", user.getToken().toString());

        UserContextInterceptor interceptor = new UserContextInterceptor(userRepository);
        boolean interceptorResult = interceptor.preHandle(request, response, handlerMethod);

        assertTrue(interceptorResult); // Check if request gets terminated
        assertEquals(response.getStatus(), HttpServletResponse.SC_OK); // Check if status is 200
    }

    @Test
    public void annotatedWithCorrectTokenHeaderButBannedIpTest() throws Exception {
        final Method method = TestController.class.getMethod("annotatedTestMethod");
        final TestController controller = new TestController();
        final MockHttpServletRequest request = new MockHttpServletRequest("POST","/testURL/");
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final HandlerMethod handlerMethod = new HandlerMethod(controller, method);

        Session session = user.getSession();
        session.banIp("130.161.128.82");
        sessionRepository.save(session);

        request.addHeader("X-USER-TOKEN", user.getToken().toString());
        request.setRemoteAddr("130.161.128.82");

        UserContextInterceptor interceptor = new UserContextInterceptor(userRepository);
        boolean interceptorResult = interceptor.preHandle(request, response, handlerMethod);

        assertTrue(interceptorResult); // Check if request gets terminated
        assertEquals(response.getStatus(), HttpServletResponse.SC_FORBIDDEN); // Check if status is 403
    }
}
