package hello.exception.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class ErrorPageController {

    //RequestDispatcher 상수로 정의되어 있음
    public static final String ERROR_EXCEPTION ="javax.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE ="javax.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME ="javax.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE ="javax.servlet.error.status_code";

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage/404");
        printErrorInfo(request);
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage/500");
        printErrorInfo(request);
        return "error-page/500";
    }

    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    //accept type에 따라 뭐가 호출 될꺼야

    public ResponseEntity<Map<String, Object>> errorPage500Api
            (HttpServletRequest request, HttpServletResponse response) {
        log.info("API errorPage 500");

        //Jackson 라이브러리는 Map을 Json 구조로 변환 가능
        Map<String, Object> rst = new HashMap<>();
        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
        rst.put("status", request.getAttribute(ERROR_STATUS_CODE));
        rst.put("message", ex.getMessage());
        //순서 중요하지는 않음

        //HTTP 상태 코드
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return new ResponseEntity<>(rst, HttpStatus.valueOf(statusCode));
    }

    //Accept를 따로 설정 하지 않으면 applicationJson 설정 하지 않은 것이 호출

    public void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION {}", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE {}", request.getAttribute(ERROR_MESSAGE));
        log.info("ERROR_REQUEST_URI {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE {}", request.getAttribute(ERROR_STATUS_CODE));
        log.info("dispatcherType ={}", request.getDispatcherType());
    }
}
