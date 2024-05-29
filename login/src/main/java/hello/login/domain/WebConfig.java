package hello.login.domain;

import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    } //

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**") //하위 전부 모든 파일
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("", "members/add", "/login" , "/logout",
                        "/*.ico", "/error", "/css/**"); //pattern을 세밀화 할 수 있음 


    }

    //@Bean
    public FilterRegistrationBean logFilter() {
        //Filter가 WAS 올릴때 같이 등록
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1); // 순서
        filterRegistrationBean.addUrlPatterns("/*"); //pattern

        return filterRegistrationBean;
    }

    //@Bean
    public FilterRegistrationBean loginCheckFilter() {
        //Filter가 WAS 올릴때 같이 등록
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2); // 순서
        filterRegistrationBean.addUrlPatterns("/*"); //pattern

        return filterRegistrationBean;

        //filter 한번 더 호출 했다고, 성능 저하 X
    }
}
