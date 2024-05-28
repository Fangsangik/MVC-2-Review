package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    public static final String SESSION_COOKIE_NAME = "mySessionId";

    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
    //동시에 여러값이 접근할때 동시성 발생 가능성 -> ConcurrentHashMap 사용

    /**
     * 세션 생성
     */
    public void createSession(Object value, HttpServletResponse response) {
        //세션 아이디를 생성하고 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        //쿠키 생성
        Cookie myCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(myCookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies == null) {
//            return null;
//        }
//
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals(SESSION_COOKIE_NAME)){
//                return sessionStore.get(cookie.getValue());
//            }
//        }
//        return null;

        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie == null) {
            return null;
        }

        return sessionStore.get(sessionCookie.getValue());
    }

    private Cookie findCookie(HttpServletRequest request, String sessionCookieName) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies()) //배열을 stream으로 변경
                .filter(cookie -> cookie.getName().equals(sessionCookieName))
                .findAny().orElse(null);
    }

    /**
     * 만료
     */

    public void expire(HttpServletRequest request){
        Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
        if (cookie != null) {
            //만료가 되면 map에서 통으로 한줄 지워버림
            sessionStore.remove(cookie.getValue());
        }
    }
}
