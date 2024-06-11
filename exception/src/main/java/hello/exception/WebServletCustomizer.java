package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

//@Component
// -> 스프링 부트가 제공하는 기본 오류 메커니즘을 사용하기 위해 Component 주석 처리
//RuntimeException이 WAS에 전달되거나, response.sendError -> 예외 page 경로 호출
public class WebServletCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/4040"); //404 발생하면 이 page 호출
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500"); // 500 발생시 500으로 가라

        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");
        //WAS가 500 터지면 여기 호출, -> Controller
        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}
