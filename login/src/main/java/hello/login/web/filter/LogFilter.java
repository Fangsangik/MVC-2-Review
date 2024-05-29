package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        //요청 온 부분 구분
        String uuid = UUID.randomUUID().toString();

        try {
            log.info("REQUEST [{}{}]", uuid, requestURI);
            chain.doFilter(request, response); //다음 필터 호출 없으면 servlet

            //Controller에서 로그를 쭉 남김
        } catch (Exception e){
            throw  e;
        } finally {
            log.info("RESPONSE [{}{}]", uuid, requestURI);
        }

        log.info("log filter do filter");
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");

    }
}
