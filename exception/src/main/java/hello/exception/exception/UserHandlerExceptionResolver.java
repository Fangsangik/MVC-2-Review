package hello.exception.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try {

            //400 인 것과 아닌것 구분
            if (ex instanceof UserException) {
                log.info("UserException resolver to 400");
                String acceptHeader = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                if ("application/json".equals(acceptHeader)) {
                    Map<String, Object> errorRst = new HashMap<>();
                    errorRst.put("ex", ex.getClass());
                    errorRst.put("message", ex.getMessage());

                    String rst = objectMapper.writeValueAsString(errorRst); //객체를 문자로 변경

                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(rst);
                    //예외 먹어버리지만 serlvet 까지 정상흐름으로 return
                    return new ModelAndView();
                } else {
                    //TEXT/HTML 넘어올떄 -> template에 html.500
                    return new ModelAndView("error/500");
                }
            }


        } catch (IOException e) {
            log.info("resolver ex", e);
        }

        return null;
    }
}
